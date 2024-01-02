package com.softserve.framework.test;

import com.softserve.framework.library.GuestFunctions;
import com.softserve.framework.tools.LocalStorageJS;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@ExtendWith(RunnerExtension.class)
public abstract class TestRunner {

    private static final String BASE_URL = "https://www.greencity.social/";
    private static final long IMPLICITLY_WAIT_1_SECOND = 1L;
    private static final long IMPLICITLY_WAIT_10_SECONDS = 10L;
    private static final long WEBDRIVER_WAIT_10_SECONDS = 10L;
    private static final long ONE_SECOND_DELAY = 1000L;
    private static final String TIME_TEMPLATE = "yyyy-MM-dd_HH-mm-ss-S";

    protected static WebDriver driver;
    protected static boolean isTestSuccessful = false;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
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

    private void takeScreenShot() {
        logger.debug("Start takeScreenShot()");
        //
        //String currentTime = new SimpleDateFormat(TIME_TEMPLATE).format(new Date());
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_TEMPLATE);
        String currentTime = localDate.format(formatter);
        //
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("./" + currentTime + "_screenshot.png"));
        } catch (IOException e) {
            // Log.error
            throw new RuntimeException(e);
        }
    }

    private void takePageSource() {
        logger.debug("Start takePageSource()");
        //
        String currentTime = new SimpleDateFormat(TIME_TEMPLATE).format(new Date());
        String pageSource = driver.getPageSource();
        byte[] strToBytes = pageSource.getBytes();
        Path path = Paths.get("./" + currentTime + "_" + "_source.html.txt");
        try {
            Files.write(path, strToBytes, StandardOpenOption.CREATE);
        } catch (IOException e) {
            // Log.error
            throw new RuntimeException(e);
        }
    }



    protected static void closePopUp() {
        presentationSleep(2); // For Presentation ONLY
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_1_SECOND)); // 0 by default
        List<WebElement> loginFormCloseButton = driver.findElements(By.cssSelector("img[alt='close button']"));
        //System.out.println("loginFormCloseButton.size() = " + loginFormCloseButton.size());
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

        //System.out.println("@BeforeAll executed");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        //System.out.println("@AfterAll executed");
    }


    @BeforeEach
    public void setUpThis() {
        driver.get(BASE_URL);
        closePopUp();
        //System.out.println("\t@BeforeEach executed");
    }

    @AfterEach
    public void tearDownThis(TestInfo testInfo) {
        if (!isTestSuccessful) {
            // Log.error
            logger.error("Test_Name = " + testInfo.getDisplayName() + " failed");
            //
            System.out.println("\t\t\tTest_Name = " + testInfo.getDisplayName() + " fail");
            System.out.println("\t\t\tTest_Method = " + testInfo.getTestMethod() + " fail");
            //
            takeScreenShot();
            takePageSource();
        }
        presentationSleep();
        driver.manage().deleteAllCookies();

        localStorageJS.removeItemFromLocalStorage("accessToken");
        localStorageJS.removeItemFromLocalStorage("refreshToken");
        driver.navigate().refresh();
        //localStorageJS.clearLocalStorage();

        //System.out.println("\t@AfterEach executed");
    }
}
