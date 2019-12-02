package churchcrm.testframework;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends Page {
    public LoginPage(WebDriver driver) {
        super(driver, "http://digitalnizena.cz/church/Login.php");
    }

    public DashboardPage login(String username, String password) {
        driver.findElement(By.cssSelector("#UserBox")).sendKeys(username);
        driver.findElement(By.cssSelector("#PasswordBox")).sendKeys(password);
        driver.findElement(By.cssSelector(".btn-primary")).click();

        return new DashboardPage(driver);
    }

    public void shouldNotBeOpen() {
        Assert.assertEquals(driver.getCurrentUrl(), "xxxx");
        Assert.assertNotEquals(driver.getCurrentUrl(), url);
        //       Assert.assertEquals(driver.findElements(By.cssSelector(".login-box"))).isEmpty();
        }

    public void shouldBeOpen() {
        Assert.assertEquals(driver.getCurrentUrl(),url);
        //assertThat(driver.findElements(By.cssSelector(".login-box"))).isNotEmpty();
    }

    public void shouldBeErrorMessage() {
        WebElement errorDiv = driver.findElement(By.cssSelector("#Login .alert.alert-error"));    // there are in fact two .alert .alert-error boxes in page, we want that for #Login div
        Assert.assertEquals(errorDiv.getText(),"Invalid login or password");
    }
}
