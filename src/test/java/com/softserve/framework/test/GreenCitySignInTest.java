package com.softserve.framework.test;

import com.softserve.framework.data.InvalidUserRepository;
import com.softserve.framework.data.InvalidUser;
import com.softserve.framework.data.User;
import com.softserve.framework.data.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

import java.util.stream.Stream;

public class GreenCitySignInTest extends TestRunner {


    private static Stream<Arguments> signInInvalidUserDataProvider() {
        return Stream.of(
                Arguments.of(InvalidUserRepository.getInvalidUserNotConfirmed()),
                Arguments.of(InvalidUserRepository.getInvalidEmailEmpty()),
                Arguments.of(InvalidUserRepository.getInvalidEmailNoDot()),
                Arguments.of(InvalidUserRepository.getInvalidEmailCommaInsteadOfDot()),
                Arguments.of(InvalidUserRepository.getInvalidEmailNoAtSignSymbol()),
                Arguments.of(InvalidUserRepository.getInvalidEmailWithSpace()),
                Arguments.of(InvalidUserRepository.getInvalidPasswordTooShort()),
                Arguments.of(InvalidUserRepository.getInvalidPasswordEmpty()),
                Arguments.of(InvalidUserRepository.getInvalidPasswordTooLong()),
                Arguments.of(InvalidUserRepository.getInvalidPasswordEmpty()),
                Arguments.of(InvalidUserRepository.getInvalidPasswordWrong()),
                Arguments.of(InvalidUserRepository.getInvalidMailWrong())
        );
    }

    private static Stream<Arguments> signInValidUserDataProvider() {
        return Stream.of(
                Arguments.of(UserRepository.getValidUserWdd35497()),
                Arguments.of(UserRepository.getValidUserFgp14159())
        );
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
        guestFunctions.signInButtonClick();
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

    //Sign In with valid data
    @ParameterizedTest(name = "{index} => user={0}")
    @MethodSource("signInValidUserDataProvider")
    public void signInValidUser(User user) {
        guestFunctions.signIn(user.getEmail(), user.getPassword());
        String expectedUserName = user.getUsername();
        String actualUserName = driver.findElement(By.cssSelector("li.ubs-user-name")).getText();
        presentationSleep();
        Assertions.assertEquals(expectedUserName, actualUserName);
    }

    //Sign in with invalid data
    @ParameterizedTest(name = "{index} => user={0}")
    @MethodSource("signInInvalidUserDataProvider")
    public void signInInvalidUser(InvalidUser user) {
        guestFunctions.signIn(user.getEmail(), user.getPassword());;
        String expectedErrorText = user.getErrorMessage();
        String actualErrorText = guestFunctions.signInInvalidUserExpectedErrorMessage(user);
        presentationSleep();
        Assertions.assertEquals(expectedErrorText, actualErrorText);
    }
}
