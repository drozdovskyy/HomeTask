package com.softserve.edu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;


public class SignInTest {

    private static final String BASE_URL = "https://www.greencity.social/";
    private static final long IMPLICITLY_WAIT_SECONDS = 10L;
    private static final long ONE_SECOND_DELAY = 1000L;
    private static final int SCREEN_WIDTH_FOR_DESIGN_CHANGE = 1455;

    private static WebDriver driver;

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

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        driver.manage().window().setSize(new Dimension(1400,768));
        //driver.manage().window().maximize();
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
        System.out.println("\t@BeforeEach executed");
    }

    @AfterEach
    public void tearDownThis() {
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
        //assertThat(welcomeText.getText(), is("Welcome back!"));
        //assertThat(signInDetailsText.getText(), is("Please enter your details to sign in."));
        //assertThat(emailLabel.getText(), is("Email"));
        //assertThat(passwordLabel.getText(), is("Password"));
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
    @ParameterizedTest
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
    @ParameterizedTest
    @CsvSource({"wdd35497@nezid.com, weyt3$Guew^, Wdd35497"})
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

        driver.findElement(By.cssSelector("app-ubs .ubs-user-name")).click();
        presentationSleep();

        driver.findElement(By.cssSelector("app-ubs [aria-label='sign-out'] a")).click();
        presentationSleep();


    }

    //Not registered User, valid email and password
    @ParameterizedTest
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
    @ParameterizedTest
    @CsvSource({
            "wdd35497nezid.com, weyt3$Guew^, 'Please check if the email is written correctly'",
            "wdd35497@nezidcom, weyt3$Guew^, 'Please check if the email is written correctly'",
            "wdd35497nezidcom, weyt3$Guew^, 'Please check if the email is written correctly'",
            "'wdd35497@nezid,com', weyt3$Guew^, 'Please check if the email is written correctly'",
            "'wdd35497 @nezid.com', weyt3$Guew^, 'Please check if the email is written correctly'",
            "'', weyt3$Guew, 'Email is required'"

    })
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
    @ParameterizedTest
    @CsvSource({
            "wdd35497@nezid.com, w3$G, 'Password must be at least 8 characters long without spaces'",
            "wdd35497@nezid.com, '', 'Password is required'",
            "wdd35497@nezid.com, 1234567890a234567890b, 'Password must be less than 20 characters long without spaces.'"
    })
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
