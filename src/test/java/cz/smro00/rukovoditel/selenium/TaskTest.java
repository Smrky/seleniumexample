package cz.smro00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.UUID;

public class TaskTest extends TestConfig {

    @Test
    public void shouldCreateAndDeleteNewTask(){
        //Given
        String projectUuid = UUID.randomUUID().toString();
        String projectName = namePrefix + "-" + projectUuid;

        String taskUuid = UUID.randomUUID().toString();
        String taskName = namePrefix+"-task-"+taskUuid;

        loginTest();

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

        loginTest();

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

        //TODO Verify that using default filter (New, Open, Waiting) only 3 tasks will be shown. Change applied filter in Filter info dialog to only contain (New, Waiting) ...there are more ways how to do it (you can click small x on Open "label" to delete it, or you can deal with writing into "suggestion box"). Verify only New and Waiting tasks are displayed. Now remove all filters and verify all created tasks are displayed. Delete all tasks using Select all and batch delete.

        //deleteProject(projectName);
    }

}
