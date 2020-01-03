package cz.smro00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ProjectTest extends TestConfig {

    @Test
    public void shouldNotCreateProjectEmptyName() {
        //Given
        loginTest();

        WebElement projectsLink = driver.findElement(By.cssSelector(".fa-reorder"));
        projectsLink.click();


        //When
        WebElement addProjectButton = driver.findElement(By.cssSelector(".btn-primary"));
        addProjectButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        WebElement projectNameInput = driver.findElement(By.cssSelector("#fields_158"));
        projectNameInput.clear();

        WebElement submitProjectButton = driver.findElement(By.cssSelector(".btn-primary-modal-action"));
        submitProjectButton.click();

        //Then
        WebElement projectNameErrorLabel = driver.findElement(By.cssSelector("#fields_158-error"));
        Assert.assertEquals("This field is required!", projectNameErrorLabel.getText());
    }

    @Test
    public void shouldCreateAndDeleteNewProject() {
        //Given
        loginTest();

        // delete all projects with prefix if such exist
        searchProject("smro00");

        // if search is empty - table has only one cell with "No Records Found"
        List<WebElement> tableCells = driver.findElements(By.cssSelector(".table tbody tr td"));
        if(tableCells.size() > 1) {
            List<WebElement> tableProjectNames = driver.findElements(By.cssSelector(".table tbody tr .field-158-td"));
            List<String> projectNames = new ArrayList<>();

            for (WebElement tableProjectName : tableProjectNames) {
                projectNames.add(tableProjectName.getText());
            }

            for (String projectName : projectNames) {
                deleteProject(projectName);
            }
        }

        //When
        WebElement addProjectButton = driver.findElement(By.cssSelector(".btn-primary"));
        addProjectButton.click();


        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        WebElement projectPrioritySelectElement = driver.findElement(By.cssSelector("#fields_156"));
        Select projectPrioritySelect = new Select(projectPrioritySelectElement);
        projectPrioritySelect.selectByValue("35");

        WebElement projectStatusSelectElement = driver.findElement(By.cssSelector("#fields_157"));
        Select projectStatusSelect = new Select(projectStatusSelectElement);
        projectStatusSelect.selectByValue("37");

        String uuid = UUID.randomUUID().toString();
        String projectName = "smro00-"+uuid;
        WebElement projectNameInput = driver.findElement(By.cssSelector("#fields_158"));
        projectNameInput.sendKeys(projectName);

        WebElement projectDateInput = driver.findElement(By.cssSelector("#fields_159"));
        Date date = new Date();
        SimpleDateFormat dashFormat = new SimpleDateFormat("yyyy-MM-dd");
        projectDateInput.sendKeys(dashFormat.format(date));

        WebElement submitProjectButton = driver.findElement(By.cssSelector(".btn-primary-modal-action"));
        submitProjectButton.click();

        //Then
        Assert.assertEquals("Rukovoditel | Tasks", driver.getTitle());
        WebElement breadcrumbItem = driver.findElement(By.cssSelector(".page-breadcrumb li:nth-child(2)"));
        Assert.assertEquals(projectName, breadcrumbItem.getText());

        searchProject(projectName);

        WebElement projectPriorityDiv = driver.findElement(By.cssSelector(".field-156-td"));
        WebElement projectStatusDiv = driver.findElement(By.cssSelector(".field-157-td"));
        WebElement projectNameLink = driver.findElement(By.cssSelector(".field-158-td"));
        WebElement projectStartDate = driver.findElement(By.cssSelector(".field-159-td"));
        SimpleDateFormat slashFormat = new SimpleDateFormat("MM/dd/yyyy");
        String checkDateFormat = slashFormat.format(date);

        Assert.assertEquals("High", projectPriorityDiv.getText());
        Assert.assertEquals("New", projectStatusDiv.getText());
        Assert.assertEquals(projectName, projectNameLink.getText());
        Assert.assertEquals(checkDateFormat, projectStartDate.getText());

        deleteProject(projectName);

        searchProject(projectName);

        tableCells = driver.findElements(By.cssSelector(".table tbody tr td"));
        Assert.assertEquals(1, tableCells.size());
    }
}
