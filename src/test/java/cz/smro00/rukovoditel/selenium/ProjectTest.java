package cz.smro00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.Date;
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

        WebDriverWait wait = new WebDriverWait(driver, 1);
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

        driver.get(URL+"index.php?module=items/items&path=21");

        //When
        WebElement addProjectButton = driver.findElement(By.cssSelector(".btn-primary"));
        addProjectButton.click();

        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        WebElement projectPrioritySelectElement = driver.findElement(By.cssSelector("#fields_156"));
        Select projectPrioritySelect = new Select(projectPrioritySelectElement);
        projectPrioritySelect.selectByValue("35");

        WebElement projectStatusSelectElement = driver.findElement(By.cssSelector("#fields_157"));
        Select projectStatusSelect = new Select(projectStatusSelectElement);
        projectStatusSelect.selectByValue("37");

        String uuid = UUID.randomUUID().toString();
        WebElement projectNameInput = driver.findElement(By.cssSelector("#fields_158"));
        projectNameInput.sendKeys("smro00 - "+uuid);

        WebElement projectDateInput = driver.findElement(By.cssSelector("#fields_159"));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        projectDateInput.sendKeys(sdf.format(date));

        WebElement submitProjectButton = driver.findElement(By.cssSelector(".btn-primary-modal-action"));
        submitProjectButton.click();

        //Then
        Assert.assertEquals("Rukovoditel | Tasks", driver.getTitle());
        driver.findElement(By.cssSelector(".page-breadcrumb li"));


        WebElement projectsLink = driver.findElement(By.cssSelector(".fa-reorder"));
        projectsLink.click();

        WebElement projectSearchInput = driver.findElement(By.cssSelector("#entity_items_listing66_21_search_keywords"));
        projectSearchInput.clear();
        projectSearchInput.sendKeys(uuid);


    }
}
