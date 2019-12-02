package churchcrm.testframework;

import org.openqa.selenium.WebDriver;

public class Page {

    protected String url;
    protected WebDriver driver;

    public Page(WebDriver driver, String url) {
        this.driver = driver;
        this.url = url;
    }

    public void open() {
        driver.get(url);
    }

    public void shouldBeOpen() {

    }

}
