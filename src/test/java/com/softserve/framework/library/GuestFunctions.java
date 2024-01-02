package com.softserve.framework.library;

import com.softserve.framework.data.InvalidUser;
import com.softserve.framework.data.User;
import com.softserve.framework.test.TestRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuestFunctions {
    public static final Logger logger = LoggerFactory.getLogger(GuestFunctions.class);
    private WebDriver driver;
    private static final int SCREEN_WIDTH_FOR_DESIGN_CHANGE = 1455;


    public GuestFunctions(WebDriver driver) {
        this.driver = driver;
    }

    public void signInButtonClick() {
        logger.debug("Start signIn Button click");
        Dimension screenSize = driver.manage().window().getSize();
        if (screenSize.getWidth() > SCREEN_WIDTH_FOR_DESIGN_CHANGE) {
            driver.findElement(By.cssSelector("app-ubs .ubs-header-sign-in")).click(); // Big Sign In button
        } else {
            driver.findElement(By.cssSelector("app-ubs .ubs-header-sing-in-img")).click(); // Small Sign In button
        }
        logger.debug("Done signIn Button click");
    }

    public void signIn(User user) {
        logger.debug("Start signIn with user = " + user);
        signInButtonClick();

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(user.getEmail());
        TestRunner.presentationSleep();

        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(user.getPassword());
        TestRunner.presentationSleep();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        TestRunner.presentationSleep();
        logger.debug("Done signIn with user = " + user);
    }

    public void signIn(InvalidUser invalidUser) {
        logger.debug("Start signIn with invalidUser = " + invalidUser);
        signInButtonClick();

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(invalidUser.getEmail());
        TestRunner.presentationSleep();

        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(invalidUser.getPassword());
        TestRunner.presentationSleep();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        TestRunner.presentationSleep();
        logger.debug("Done signIn with invalidUser = " + invalidUser);
    }



    public String signInInvalidUserExpectedErrorMessage(InvalidUser invalidUser) {

        logger.debug("Start signInInvalidUserExpectedErrorMessage with user = " + invalidUser.getEmail());
        return switch (invalidUser.getErrorLocalization()) {
            case "email"-> driver.findElement(By.cssSelector("#email-err-msg div")).getText();
            case "password"-> driver.findElement(By.cssSelector("#pass-err-msg div")).getText();
            case "general"-> driver.findElement(By.cssSelector(".alert-general-error")).getText();
            default -> "Error in InvalidUser errorLocalization";
        };

    }


}
