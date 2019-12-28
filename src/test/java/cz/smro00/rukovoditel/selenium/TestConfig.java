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

public class TestConfig {
    public ChromeDriver driver;
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
    }

    @After
    public void tearDown() {
        driver.close();
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
        //Given
        rukovoditelLogin("rukovoditel", "vse456ru");

        //Then
        Assert.assertEquals("Rukovoditel | Dashboard", driver.getTitle());
        Assert.assertTrue(driver.getTitle().contentEquals("Rukovoditel | Dashboard"));

        WebElement userDropdown = driver.findElement(By.cssSelector(".dropdown.user"));
        Assert.assertTrue(userDropdown.isDisplayed());

        WebElement usernameSpan = driver.findElement(By.cssSelector(".username"));
        Assert.assertEquals("System Administrator", usernameSpan.getText());
    }
}
