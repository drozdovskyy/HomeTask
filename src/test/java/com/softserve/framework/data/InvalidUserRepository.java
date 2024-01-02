package com.softserve.framework.data;

import com.softserve.framework.tools.CSVReader;

import java.util.List;

public final class InvalidUserRepository {
    private InvalidUserRepository() {
    }

    public static InvalidUser getDefault() {
        return getInvalidPasswordTooShort();
    }

    public static InvalidUser getInvalidUserNotConfirmed() {
        return new InvalidUser("samplestest@greencity.com",
                "weyt3$Guew^",
                "You should verify the email first, check your email box!",
                "general");
    }

    public static InvalidUser getInvalidEmailNoAtSignSymbol() {
        return new InvalidUser(
                "wdd35497nezid.com", "weyt3$Guew^", "Please check if the email is written correctly", "email");
    }

    public static InvalidUser getInvalidEmailNoDot() {
        return new InvalidUser(
                "wdd35497@nezidcom", "weyt3$Guew^", "Please check if the email is written correctly", "email");
    }

   public static InvalidUser getInvalidEmailCommaInsteadOfDot() {
        return new InvalidUser(
                "wdd35497@nezid,com", "weyt3$Guew^", "Please check if the email is written correctly", "email");
    }

    public static InvalidUser getInvalidEmailWithSpace() {
        return new InvalidUser(
                "wdd35497 @nezid.com", "weyt3$Guew^", "Please check if the email is written correctly", "email");
    }

    public static InvalidUser getInvalidEmailEmpty() {
        return new InvalidUser(
                "", "weyt3$Guew", "Email is required", "email");
    }


    public static InvalidUser getInvalidPasswordTooShort() {
        return new InvalidUser(
                "wdd35497@nezid.com",
                "w3$G",
                "Password must be at least 8 characters long without spaces",
                "password");
    }

    public static InvalidUser getInvalidPasswordEmpty() {
        return new InvalidUser(
                "wdd35497@nezid.com",
                "",
                "Password is required",
                "password");
    }

    public static InvalidUser getInvalidPasswordTooLong() {
        return new InvalidUser(
                "wdd35497@nezid.com",
                "1234567890a234567890b",
                "Password must be less than 20 characters long without spaces.",
                "password");
    }

    public static InvalidUser getInvalidPasswordWrong() {
        return new InvalidUser(
                "wdd35497@nezid.com",
                "weyt3$Guew",
                "Bad email or password",
                "general");
    }

    public static InvalidUser getInvalidMailWrong() {
        return new InvalidUser(
                "wdd35497@nezi.com",
                "weyt3$Guew^",
                "Bad email or password",
                "general");
    }

    public static List<InvalidUser> fromCsv(String filename) {
        return InvalidUser.getByLists(new CSVReader(filename).getAllCells());
    }

    public static List<InvalidUser> fromCsv() {
        return fromCsv("invalid_users.csv");
    }
}
