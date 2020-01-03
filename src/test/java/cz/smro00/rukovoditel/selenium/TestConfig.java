package cz.smro00.rukovoditel.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestConfig {
    public ChromeDriver driver;
    public WebDriverWait wait;
    final String URL = "https://digitalnizena.cz/rukovoditel/";
    final String namePrefix="smro00";

    boolean runOnTravis = false;
    boolean windows = false;

    @Before
    public void init() {
        ChromeOptions cho = new ChromeOptions();


        if (runOnTravis) {
            cho.addArguments("headless");
        } else {
            if (windows) {
                System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
            } else {
                System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
            }
        }
//        ChromeDriverService service = new ChromeDriverService()
        driver = new ChromeDriver(cho);
        wait = new WebDriverWait(driver, 1);
    }


    @After
    public void tearDown() {
        //driver.close();
    }

    //Methods

    public void rukovoditelLogin(String username, String password) {
        driver.get(URL);

        WebElement usernameInput = driver.findElement(By.cssSelector("input[name=username]"));
        usernameInput.sendKeys(username);

        WebElement passwordInput = driver.findElement(By.cssSelector("input[name=password]"));
        passwordInput.sendKeys(password);

        WebElement loginButton = driver.findElement(By.cssSelector(".btn"));
        loginButton.click();
    }

    public void loginTest() {
        //Given + When (user logs in to page)
        rukovoditelLogin("rukovoditel", "vse456ru");

        //Then (check user is logged)
        Assert.assertEquals("Rukovoditel | Dashboard", driver.getTitle());
        Assert.assertTrue(driver.getTitle().contentEquals("Rukovoditel | Dashboard"));

        WebElement userDropdown = driver.findElement(By.cssSelector(".dropdown.user"));
        Assert.assertTrue(userDropdown.isDisplayed());

        WebElement usernameSpan = driver.findElement(By.cssSelector(".username"));
        Assert.assertEquals("System Administrator", usernameSpan.getText());
    }

    public void deleteProject(String projectName) {
        searchProject(projectName);

        WebElement deleteProjectIcon = driver.findElement(By.cssSelector(".table .fa-trash-o"));
        deleteProjectIcon.click();

        //wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        WebElement confirmDeleteDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#uniform-delete_confirm")));
        confirmDeleteDiv.click();
        WebElement submitDeleteProjectButton = driver.findElement(By.cssSelector(".btn-primary-modal-action"));
        submitDeleteProjectButton.click();
    }

    public void deleteAllProjects(String searchName) {
        searchProject(searchName);

        WebElement selectAllItemsInput = driver.findElement(By.cssSelector("#select_all_items"));
        selectAllItemsInput.click();

        WebElement projectMoreActionsToggle = driver.findElement(By.cssSelector(".entitly-listing-buttons-left .dropdown-toggle"));
        projectMoreActionsToggle.click();

        WebElement deleteProjectIcon = driver.findElement(By.cssSelector(".entitly-listing-buttons-left .fa-trash-o"));
        deleteProjectIcon.click();

        WebElement confirmDeleteDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#uniform-delete_confirm")));
        confirmDeleteDiv.click();
        WebElement submitDeleteProjectButton = driver.findElement(By.cssSelector(".btn-primary-modal-action"));
        submitDeleteProjectButton.click();
    }

    public void searchProject(String projectName) {
        WebElement projectsLink = driver.findElement(By.cssSelector(".fa-reorder"));
        projectsLink.click();

        WebElement filtersDropdown = driver.findElement(By.cssSelector(".portlet-filters-preview .fa-angle-down"));
        filtersDropdown.click();

        WebElement defaultFilterLink = driver.findElement(By.cssSelector(".portlet-title .caption .btn-group:nth-child(1) ul li:nth-child(1) a"));
        defaultFilterLink.click();


        WebElement projectSearchInput = driver.findElement(By.cssSelector("#entity_items_listing66_21_search_keywords"));
        projectSearchInput.clear();
        projectSearchInput.sendKeys(projectName);

        WebElement projectSearchIcon = driver.findElement(By.cssSelector(".fa-search"));
        projectSearchIcon.click();


        WebElement searchTable = driver.findElement(By.cssSelector(".table"));
        wait.until(ExpectedConditions.stalenessOf(searchTable));
    }

    //Creates project with Status=New, Priority=High and Date=Today
    public void createProject(String projectName) {
        Date startDate = new Date();

        WebElement projectsLink = driver.findElement(By.cssSelector(".fa-reorder"));
        projectsLink.click();

        WebElement addProjectButton = driver.findElement(By.cssSelector(".btn-primary"));
        addProjectButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        WebElement projectPrioritySelectElement = driver.findElement(By.cssSelector("#fields_156"));
        Select projectPrioritySelect = new Select(projectPrioritySelectElement);
        //34=Urgent, 35=High
        projectPrioritySelect.selectByValue("35");

        WebElement projectStatusSelectElement = driver.findElement(By.cssSelector("#fields_157"));
        Select projectStatusSelect = new Select(projectStatusSelectElement);
        //37=New, 38=Open, 39=Waiting, 40=Closed, 41=Cancelled
        projectStatusSelect.selectByValue("37");

        WebElement projectNameInput = driver.findElement(By.cssSelector("#fields_158"));
        projectNameInput.sendKeys(projectName);

        WebElement projectDateInput = driver.findElement(By.cssSelector("#fields_159"));
        SimpleDateFormat dashFormat = new SimpleDateFormat("yyyy-MM-dd");
        projectDateInput.sendKeys(dashFormat.format(startDate));

        WebElement submitProjectButton = driver.findElement(By.cssSelector(".btn-primary-modal-action"));
        submitProjectButton.click();
    }

    public void createTaskInProject(String projectName, String taskName, String taskStatus){
        String taskDescription = "Hello world!";

        searchProject(projectName);

        WebElement projectLink = driver.findElement(By.cssSelector(".item_heading_link"));
        projectLink.click();

        WebElement addTaskButton = driver.findElement(By.cssSelector(".btn-primary"));
        addTaskButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        WebElement taskTypeSelectElement = driver.findElement(By.cssSelector("#fields_167"));
        Select taskTypeSelect = new Select(taskTypeSelectElement);
        //45=Idea, 42=Task, 43=Change, 44=Bug
        taskTypeSelect.selectByValue("42");

        WebElement taskNameInput = driver.findElement(By.cssSelector("#fields_168"));
        taskNameInput.sendKeys(taskName);

        WebElement taskStatusSelectElement = driver.findElement(By.cssSelector("#fields_169"));
        Select taskStatusSelect = new Select(taskStatusSelectElement);
        //46=New, 47=Open, 48=Waiting, 49=Done, 50=Closed, 51=Paid, 52=Canceled
        taskStatusSelect.selectByValue(taskStatus);

        WebElement taskPrioritySelectElement = driver.findElement(By.cssSelector("#fields_170"));
        Select taskPrioritySelect = new Select(taskPrioritySelectElement);
        //53=Urgent, 54=High, 55=Medium
        taskPrioritySelect.selectByValue("55");

        WebElement descriptionIFrame = driver.findElement(By.cssSelector(".cke_wysiwyg_frame"));
        driver.switchTo().frame(descriptionIFrame);
        WebElement descriptionBody = driver.findElement(By.cssSelector(".cke_contents_ltr"));
        descriptionBody.sendKeys(taskDescription);

        driver.switchTo().defaultContent();
        WebElement createTaskButton = driver.findElement(By.cssSelector(".btn-primary-modal-action"));
        createTaskButton.click();
    }
}
