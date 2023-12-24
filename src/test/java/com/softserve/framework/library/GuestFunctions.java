package com.softserve.framework.library;

import com.softserve.framework.data.InvalidUser;
import com.softserve.framework.test.TestRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

public class GuestFunctions {
    private WebDriver driver;
    private static final int SCREEN_WIDTH_FOR_DESIGN_CHANGE = 1455;


    public GuestFunctions(WebDriver driver) {
        this.driver = driver;
    }

    public void signInButtonClick() {
        Dimension screenSize = driver.manage().window().getSize();
        if (screenSize.getWidth() > SCREEN_WIDTH_FOR_DESIGN_CHANGE) {
            driver.findElement(By.cssSelector("app-ubs .ubs-header-sign-in")).click(); // Big Sign In button
        } else {
            driver.findElement(By.cssSelector("app-ubs .ubs-header-sing-in-img")).click(); // Small Sign In button
        }
    }

    public void signIn(String email, String password) {

        signInButtonClick();

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        TestRunner.presentationSleep();

        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        TestRunner.presentationSleep();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        TestRunner.presentationSleep();
    }

    public String signInInvalidUserExpectedErrorMessage(InvalidUser invalidUser) {

        return switch (invalidUser.getErrorLocalization()) {
            case "email"-> driver.findElement(By.cssSelector("#email-err-msg div")).getText();
            case "password"-> driver.findElement(By.cssSelector("#pass-err-msg div")).getText();
            case "general"-> driver.findElement(By.cssSelector(".alert-general-error")).getText();
            default -> "Error in InvalidUser errorLocalization";
        };

    }


}
