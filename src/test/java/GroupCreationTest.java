
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class GroupCreationTest {

    private  WebDriver driver;
    private WebDriverWait wait;
    private  static java.lang.String USERNAME;
    private static java.lang.String PASSWORD;

    @BeforeAll
    public static void setupClass(){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\geckodriver");
        USERNAME = "GB202310790789";
        PASSWORD = "0a5b2e39a5";
    }

    @BeforeEach
    public void setupTest(){
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    void testStandGeekBrains() throws IOException {
        driver.get("https://test-stand.gb.ru/login");
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("form#login input[type='text']"))).sendKeys(USERNAME);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("form#login input[type='password']"))).sendKeys(PASSWORD);

        WebElement loginButton = driver.findElement(By.cssSelector("form#login button"));
        loginButton.click();
        wait.until(ExpectedConditions.invisibilityOf(loginButton));

        WebElement usernameLink = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(USERNAME)));
        assertEquals(String.format("Hello, %s", USERNAME), usernameLink.getText().replace("\n", " ").trim());

        WebElement createUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-btn")));
        createUser.click();

        String firstName = "UserName"+ System.currentTimeMillis();
        String login = "g"+ System.currentTimeMillis();

        WebElement FirstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.
                xpath("//form//span[contains(text(), 'Fist Name')]/following-sibling::input")));
        FirstName.sendKeys(firstName);
        WebElement loginUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.
                xpath("//form//span[contains(text(), 'Login')]/following-sibling::input")));
        loginUser.sendKeys(login);
        WebElement buttonSave = driver.findElement(By.cssSelector("form div.submit button"));
        buttonSave.click();
        driver.navigate().refresh();

        String xpath = String.format("//table[@aria-label='Dummies list']/tbody//td[text()='%s']", firstName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        WebElement newUser = driver.findElement(By.xpath(xpath));
        assertTrue(newUser.getText().contains(firstName));

        byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        Files.write(Path.of("src/test/resources/screenshot" + System.currentTimeMillis() + ".png"), screenshot);
    }

    @AfterEach
    public void teardown(){
        driver.quit();
    }
}
