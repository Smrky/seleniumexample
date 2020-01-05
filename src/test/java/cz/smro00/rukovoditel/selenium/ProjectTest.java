package cz.smro00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ProjectTest extends TestConfig {

    @Test
    public void shouldNotCreateProjectEmptyName() {
        //Given
        validLogin();

        WebElement projectsLink = driver.findElement(By.cssSelector(".fa-reorder"));
        projectsLink.click();


        //When
        createProject("");


        //Then
        WebElement projectNameErrorLabel = driver.findElement(By.cssSelector("#fields_158-error"));
        Assert.assertEquals("This field is required!", projectNameErrorLabel.getText());
    }

    @Test
    public void shouldCreateAndDeleteNewProject() {
        //Given
        validLogin();

        // delete all projects with prefix if such exist
        searchProject(namePrefix);

        // if search is empty - table has only one cell with "No Records Found"
        List<WebElement> tableCells = driver.findElements(By.cssSelector(".table tbody tr td"));

        if(tableCells.size() > 1) {
            List<WebElement> tableRows = driver.findElements(By.cssSelector(".table tbody tr"));

            if(tableRows.size() == 1) {
                WebElement projectNameLink = driver.findElement(By.cssSelector(".field-158-td"));
                deleteProject(projectNameLink.getText());
            }
            if(tableRows.size() > 1) {
                deleteAllProjects(namePrefix);
            }
        }


        //When
        String uuid = UUID.randomUUID().toString();
        String projectName = namePrefix+"-"+uuid;
        Date date = new Date();

        createProject(projectName);

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
