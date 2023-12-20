package com.softserve.edu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

public class SignInWaitTest {


    private static final String BASE_URL = "https://www.greencity.social/";
    private static final long IMPLICITLY_WAIT_1_SECOND = 1L;
    private static final long IMPLICITLY_WAIT_10_SECONDS = 10L;
    private static final long WEBDRIVER_WAIT_10_SECONDS = 10L;
    private static final long ONE_SECOND_DELAY = 1000L;
    private static final int SCREEN_WIDTH_FOR_DESIGN_CHANGE = 1455;

    private static WebDriver driver;
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

    public void signInButtonClick() {
        Dimension screenSize = driver.manage().window().getSize();
        if (screenSize.getWidth() > SCREEN_WIDTH_FOR_DESIGN_CHANGE) {
            driver.findElement(By.cssSelector("app-ubs .ubs-header-sign-in")).click(); // Big Sign In button
        } else {
            driver.findElement(By.cssSelector("app-ubs .ubs-header-sing-in-img")).click(); // Small Sign In button
        }
    }

    public static void closePopUp() {
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

    private static Stream<Arguments> signInNotValidPasswordDataProvider() {
        return Stream.of(
                Arguments.of("wdd35497@nezid.com", "w3$G", "Password must be at least 8 characters long without spaces"),
                Arguments.of("wdd35497@nezid.com", "", "Password is required"),
                Arguments.of("wdd35497@nezid.com", "1234567890a234567890b", "Password must be less than 20 characters long without spaces.")
        );
    }
    private static Stream<Arguments> signInRegisteredUserConfirmedEmailDataProvider() {
        return Stream.of(
                Arguments.of("wdd35497@nezid.com", "weyt3$Guew^", "Wdd35497"),
                Arguments.of("fgp14159@zbock.com", "T9jqcxx#", "Fgp14159")
        );
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
        js.executeScript("window.localStorage.removeItem('accessToken')");
        js.executeScript("window.localStorage.removeItem('refreshToken')");
        driver.navigate().refresh();
        //js.executeScript("window.localStorage.clear();");

        System.out.println("\t@AfterEach executed");
    }

    //Check webpage title
    @Test
    public void verifyTitle() {
        String expectedTitle = "GreenCity";
        String actualTitle = driver.getTitle();
        Assertions.assertEquals(expectedTitle, actualTitle);
        presentationSleep();
    }

    //Check greetings and labels text
    @Test
    public void verifyGreetingsAndLabels() {
        signInButtonClick();
        String actualWelcomeText = driver.findElement(By.cssSelector("app-sign-in h1")).getText();
        String expectedWelcomeText = "Welcome back!";
        Assertions.assertEquals(expectedWelcomeText, actualWelcomeText);

        String actualSignInDetailsText = driver.findElement(By.cssSelector("app-sign-in h2")).getText();
        String expectedSignInDetailsText = "Please enter your details to sign in.";
        Assertions.assertEquals(expectedSignInDetailsText, actualSignInDetailsText);

        String actualEmailLabelText = driver.findElement(By.cssSelector("label[for='email']")).getText();
        String expectedEmailLabelText = "Email";
        Assertions.assertEquals(expectedEmailLabelText, actualEmailLabelText);

        String actualPasswordLabelText = driver.findElement(By.cssSelector("label[for='password']")).getText();
        String expectedPasswordLabelText = "Password";
        Assertions.assertEquals(expectedPasswordLabelText, actualPasswordLabelText);

        presentationSleep(2);
    }

    //Registered User, not confirmed email
    @ParameterizedTest(name = "{index} => email={0}, password={1}, expectedErrorText={2}")
    @CsvSource({"samplestest@greencity.com, weyt3$Guew^, 'You should verify the email first, check your email box!'"})
    public void signInRegisteredUserNotConfirmedEmail(String email, String password, String expectedErrorText) {
        signInButtonClick();

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        presentationSleep();

        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        presentationSleep();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        presentationSleep();

        String actualErrorText = driver.findElement(By.cssSelector(".alert-general-error")).getText();
        presentationSleep();

        Assertions.assertEquals(expectedErrorText, actualErrorText);
    }

    //Registered User, confirmed email
    @ParameterizedTest(name = "{index} => email={0}, password={1}, userName={2}")
    @MethodSource("signInRegisteredUserConfirmedEmailDataProvider")
    public void signInRegisteredUserConfirmedEmail(String email, String password, String userName) {
        signInButtonClick();

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        presentationSleep();

        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        presentationSleep();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        presentationSleep();

        String actualUserName = driver.findElement(By.cssSelector("li.ubs-user-name")).getText();
        presentationSleep();

        Assertions.assertEquals(userName,actualUserName);

/*        driver.findElement(By.cssSelector("app-ubs .ubs-user-name")).click();
        presentationSleep();

        driver.findElement(By.cssSelector("app-ubs [aria-label='sign-out'] a")).click();
        presentationSleep();
*/

    }

    //Not registered User, valid email and password
    @ParameterizedTest(name = "{index} => email={0}, password={1}, expectedErrorText={2}")
    @CsvSource({
            "wdd35497@nezid.com, weyt3$Guew, 'Bad email or password'",
            "wdd35497@nezi.com, weyt3$Guew^, 'Bad email or password'",
            "wdd35497@nezi.com, weyt3$Guew, 'Bad email or password'"
    })
    public void signInNotRegisteredUser(String email, String password, String expectedErrorText) {
        signInButtonClick();

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        presentationSleep();

        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        presentationSleep();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        presentationSleep();

        String actualErrorText = driver.findElement(By.cssSelector(".alert-general-error")).getText();
        presentationSleep();

        Assertions.assertEquals(expectedErrorText, actualErrorText);
    }

    //Not valid email
    @ParameterizedTest(name = "{index} => email={0}, password={1}, expectedErrorText={2}")
    @CsvFileSource(resources = "/test_data_not_valid_mail.csv")
    public void signInNotValidMail(String email, String password, String expectedErrorText) {
        signInButtonClick();

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        presentationSleep();

        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        presentationSleep();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        presentationSleep();

        String actualErrorText = driver.findElement(By.cssSelector("#email-err-msg div")).getText();
        presentationSleep();

        Assertions.assertEquals(expectedErrorText, actualErrorText);
    }

    //Not valid password
    @ParameterizedTest(name = "{index} => email={0}, password={1}, expectedErrorText={2}")
    @MethodSource("signInNotValidPasswordDataProvider")
    public void signInNotValidPassword(String email, String password, String expectedErrorText) {
        signInButtonClick();

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        presentationSleep();

        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        presentationSleep();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        presentationSleep();

        String actualErrorText = driver.findElement(By.cssSelector("#pass-err-msg div")).getText();
        presentationSleep();

        Assertions.assertEquals(expectedErrorText, actualErrorText);
    }
}
