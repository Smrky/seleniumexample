package cz.smro00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.UUID;

public class TaskTest extends TestConfig {

    @Test
    public void shouldCreateAndDeleteNewTask(){
        //Given
        String projectUuid = UUID.randomUUID().toString();
        String projectName = namePrefix + "-" + projectUuid;

        String taskUuid = UUID.randomUUID().toString();
        String taskName = namePrefix+"-task-"+taskUuid;

        validLogin();

        createProject(projectName);

        //When
            //taskStatus: 46=New, 47=Open, 48=Waiting, 49=Done, 50=Closed, 51=Paid, 52=Canceled
        createTaskInProject(projectName, taskName, "46");

        //Then
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".fa-info")));
        WebElement taskInfoIcon = driver.findElement(By.cssSelector(".fa-info"));
        taskInfoIcon.click();

        WebElement taskTypeCell = driver.findElement(By.cssSelector(".form-group-167 td"));
        WebElement taskNameDiv = driver.findElement(By.cssSelector(".caption"));
        WebElement taskStatusCell = driver.findElement(By.cssSelector(".form-group-169 td"));
        WebElement taskPriorityCell = driver.findElement(By.cssSelector(".form-group-170 td"));
        WebElement taskDescriptionDiv = driver.findElement(By.cssSelector(".fieldtype_textarea_wysiwyg"));

        Assert.assertEquals("Task", taskTypeCell.getText());
        Assert.assertEquals(taskName,taskNameDiv.getText());
        Assert.assertEquals("New", taskStatusCell.getText());
        Assert.assertEquals("Medium", taskPriorityCell.getText());
        Assert.assertEquals("Hello world!", taskDescriptionDiv.getText());

        WebElement taskMoreActionsToggle = driver.findElement(By.cssSelector(".prolet-body-actions .dropdown-toggle"));
        taskMoreActionsToggle.click();

        WebElement deleteTaskIcon = driver.findElement(By.cssSelector(".fa-trash-o"));
        deleteTaskIcon.click();

        WebElement confirmDeleteTaskButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#delete_item_form button.btn-primary-modal-action")));
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteTaskButton));
        confirmDeleteTaskButton.click();

        deleteProject(projectName);
    }

    @Test
    public void shouldCreate7TasksAndCheckFiltersAndDeleteTasks() {
        //Given
        String projectUuid = UUID.randomUUID().toString();
        String projectName = namePrefix + "-" + projectUuid;

        String taskPrefix = namePrefix+"-task-";

        validLogin();

        createProject(projectName);

        //When
            //taskStatus: 46=New, 47=Open, 48=Waiting, 49=Done, 50=Closed, 51=Paid, 52=Canceled
        createTaskInProject(projectName, taskPrefix+"Status-New", "46");
        createTaskInProject(projectName, taskPrefix+"Status-Open", "47");
        createTaskInProject(projectName, taskPrefix+"Status-Waiting", "48");
        createTaskInProject(projectName, taskPrefix+"Status-Done", "49");
        createTaskInProject(projectName, taskPrefix+"Status-Closed", "50");
        createTaskInProject(projectName, taskPrefix+"Status-Paid", "51");
        createTaskInProject(projectName, taskPrefix+"Status-Canceled", "52");

        //Then
        WebElement filtersDropdown = driver.findElement(By.cssSelector(".portlet-filters-preview .fa-angle-down"));
        filtersDropdown.click();

        WebElement defaultFilterLink = driver.findElement(By.cssSelector(".portlet-title .caption .btn-group:nth-child(1) ul li:nth-child(1) a"));
        defaultFilterLink.click();

        String[] expectedStatusesArray = {"New", "Open", "Waiting"};

        checkFilterResults(expectedStatusesArray, 3);

        WebElement filtersEditSpan = driver.findElement(By.cssSelector(".filters-preview-box-heading"));
        filtersEditSpan.click();

        WebElement deleteOpenFilterLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#reports_filters .search-choice a[data-option-array-index=\"1\"]")));
        deleteOpenFilterLink.click();
        WebElement submitFilterChangeButton = driver.findElement(By.cssSelector("#reports_filters .btn-primary-modal-action"));
        submitFilterChangeButton.click();

        expectedStatusesArray = new String[] {"New", "Waiting"};
        checkFilterResults(expectedStatusesArray, 2);

        WebElement deleteFiltersIcon = driver.findElement(By.cssSelector(".portlet-body .fa-trash-o"));
        deleteFiltersIcon.click();

        expectedStatusesArray = new String[] {"New", "Open", "Waiting", "Done", "Closed", "Paid", "Canceled"};
        checkFilterResults(expectedStatusesArray, 7);

        WebElement selectAllItemsInput = driver.findElement(By.cssSelector("#select_all_items"));
        selectAllItemsInput.click();

        WebElement taskMoreActionsToggle = driver.findElement(By.cssSelector(".entitly-listing-buttons-left .dropdown-toggle"));
        taskMoreActionsToggle.click();

        WebElement deleteProjectIcon = driver.findElement(By.cssSelector(".entitly-listing-buttons-left .fa-trash-o"));
        deleteProjectIcon.click();

        WebElement submitDeleteTaskButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-primary-modal-action")));
        submitDeleteTaskButton.click();

        deleteProject(projectName);
    }

}
