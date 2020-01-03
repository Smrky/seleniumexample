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
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestConfig {
    public ChromeDriver driver;
    public WebDriverWait wait;
    final String URL = "https://digitalnizena.cz/rukovoditel/";

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

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        WebElement confirmDeleteDiv = driver.findElement(By.cssSelector("#uniform-delete_confirm"));
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
}
