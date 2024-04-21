import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupCreationTest {
    private WebDriver driver;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/geckodriver");
    }

    @BeforeEach
    public void setUp() {
        driver = new FirefoxDriver();
        driver.get("https://test-stand.gb.ru/login");
        driver.findElement(By.name("username")).sendKeys("GB202310790789");
        driver.findElement(By.name("password")).sendKeys("0a5b2e39a5");
        driver.findElement(By.tagName("button")).click();
    }

    @Test
    public void testGroupCreation() throws IOException {
        //WebDriverWait wait = new WebDriverWait(driver, 10);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Нажимаем на '+' для добавления группы
        driver.findElement(By.cssSelector("create-button")).click();

        // Вводим имя новой группы

        String firstName = "UserName"+ System.currentTimeMillis();
        String login = "g"+ System.currentTimeMillis();

        WebElement FirstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.
                xpath("//form//span[contains(text(), 'Fist Name')]/following-sibling::input")));
        FirstName.sendKeys(firstName);
        WebElement loginUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.
                xpath("//form//span[contains(text(), 'Login')]/following-sibling::input")));
        loginUser.sendKeys(login);

        // Нажимаем кнопку SAVE
        driver.findElement(By.id("save-button")).click();
        driver.navigate().refresh();

        // Проверяем, что группа с именем появилась в таблице

        String xpath = String.format("//table[@aria-label='Dummies list']/tbody//td[text()='%s']", firstName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        WebElement newUser = driver.findElement(By.xpath(xpath));
        assertTrue(newUser.getText().contains(firstName));

        // Сохраняем скриншот окна браузера
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path screenshotPath = Paths.get("src/test/resources/screenshot.png");
        Files.copy(screenshot.toPath(), screenshotPath);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

