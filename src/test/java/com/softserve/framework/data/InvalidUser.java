package com.softserve.framework.data;

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
}
