package cz.smro00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginTest extends TestConfig {

    @Test
    public void shouldLoginValidCredentials() {
        //Given + When
        validLogin();

        //Then (check user is logged)
        Assert.assertEquals("Rukovoditel | Dashboard", driver.getTitle());
        Assert.assertTrue(driver.getTitle().contentEquals("Rukovoditel | Dashboard"));

        WebElement userDropdown = driver.findElement(By.cssSelector(".dropdown.user"));
        Assert.assertTrue(userDropdown.isDisplayed());

        WebElement usernameSpan = driver.findElement(By.cssSelector(".username"));
        Assert.assertEquals("System Administrator", usernameSpan.getText());
    }

    @Test
    public void shouldNotLoginInvalidCredentials() {
        //Given
        rukovoditelLogin("invalidUser", "invalidPass");

        //Then
        Assert.assertTrue(driver.getTitle().contentEquals("Rukovoditel"));

        WebElement loginErrorDiv = driver.findElement(By.cssSelector(".alert"));
        Assert.assertTrue(loginErrorDiv.isDisplayed());
        Assert.assertTrue(loginErrorDiv.getText().contains("No match for Username and/or Password."));
    }

    @Test
    public void shouldLogoffLoggedUser() {
        //Given
        validLogin();

        //When
        WebElement dropdownToggleLink = driver.findElement(By.cssSelector(".dropdown.user .dropdown-toggle"));
        dropdownToggleLink.click();

        WebElement logoffLink = driver.findElement(By.cssSelector(".fa-sign-out"));
        logoffLink.click();

        //Then
        Assert.assertTrue(driver.getTitle().contentEquals("Rukovoditel"));

        WebElement loginForm = driver.findElement(By.cssSelector("#login_form"));
        Assert.assertTrue(loginForm.isDisplayed());
    }
}
