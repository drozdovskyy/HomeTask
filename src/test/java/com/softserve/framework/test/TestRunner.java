package com.softserve.framework.test;

import com.softserve.framework.library.GuestFunctions;
import com.softserve.framework.tools.LocalStorageJS;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class TestRunner {

    private static final String BASE_URL = "https://www.greencity.social/";
    private static final long IMPLICITLY_WAIT_1_SECOND = 1L;
    private static final long IMPLICITLY_WAIT_10_SECONDS = 10L;
    private static final long WEBDRIVER_WAIT_10_SECONDS = 10L;
    private static final long ONE_SECOND_DELAY = 1000L;

    protected static WebDriver driver;
    protected static GuestFunctions guestFunctions;
    protected static LocalStorageJS localStorageJS;

    JavascriptExecutor js = (JavascriptExecutor) driver;

    public static void presentationSleep() {
        presentationSleep(1);
    }
    public static void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY); // For Presentation ONLY
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    protected static void closePopUp() {
        presentationSleep(2); // For Presentation ONLY
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_1_SECOND)); // 0 by default
        List<WebElement> loginFormCloseButton = driver.findElements(By.cssSelector("img[alt='close button']"));
        System.out.println("loginFormCloseButton.size() = " + loginFormCloseButton.size());
        if (!loginFormCloseButton.isEmpty()) {
            WebElement closeButton = loginFormCloseButton.get(0);
            closeButton.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(IMPLICITLY_WAIT_10_SECONDS));
            wait.until(ExpectedConditions.stalenessOf(closeButton));
            //presentationSleep();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_10_SECONDS)); // 0 by default
        presentationSleep(); // For Presentation ONLY


    }



    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_10_SECONDS));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(180));
        driver.manage( ).timeouts().scriptTimeout(Duration.ofSeconds(100));

        // Screen size for adaptive design test
        //driver.manage().window().setSize(new Dimension(1400,768));

        //Full screen test
        driver.manage().window().maximize();

        guestFunctions = new GuestFunctions(driver);
        localStorageJS = new LocalStorageJS(driver);

        System.out.println("@BeforeAll executed");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        System.out.println("@AfterAll executed");
    }


    @BeforeEach
    public void setUpThis() {
        driver.get(BASE_URL);
        closePopUp();
        System.out.println("\t@BeforeEach executed");
    }

    @AfterEach
    public void tearDownThis() {
        presentationSleep();
        driver.manage().deleteAllCookies();

        localStorageJS.removeItemFromLocalStorage("accessToken");
        localStorageJS.removeItemFromLocalStorage("refreshToken");
        driver.navigate().refresh();
        //localStorageJS.clearLocalStorage();

        System.out.println("\t@AfterEach executed");
    }
}
