package com.softserve.framework.data;

import java.util.ArrayList;
import java.util.List;

public class InvalidUser {
    private String email;
    private String password;
    private String errorMessage;
    private String errorLocalization;

    public InvalidUser(String email, String password, String errorMessage, String errorLocalization) {
        this.email = email;
        this.password = password;
        this.errorMessage = errorMessage;
        this.errorLocalization = errorLocalization;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorLocalization() {
        return errorLocalization;
    }

    @Override
    public String toString() {
        return "InvalidUser{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorLocalization='" + errorLocalization + '\'' +
                '}';
    }

    public static InvalidUser getByList(List<String> row) {
        return new InvalidUser(row.get(0), row.get(1), row.get(2), row.get(3));
    }

    public static List<InvalidUser> getByLists(List<List<String>> rows) {
        //System.out.println("Start List<User> getByLists rows = " + rows);
        List<InvalidUser> invalidUsers = new ArrayList<>();
        if (!rows.get(0).get(0).contains("@")) {
            rows.remove(0);
        }
        for (List<String> row : rows) {
            invalidUsers.add(getByList(row));
        }
        //System.out.println("Done List<User> getByLists users = " + users);
        return invalidUsers;
    }
}
