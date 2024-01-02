package com.softserve.framework.data;

import com.softserve.framework.tools.CSVReader;

import java.util.List;

public final class UserRepository {
    private UserRepository() {
    }
    public static User getDefault() {
        return getValidUserWdd35497();
    }
    public static User getValidUserWdd35497() {
        return new User("wdd35497@nezid.com", "weyt3$Guew^", "Wdd35497");
    }
    public static User getValidUserFgp14159() {
        return new User("fgp14159@zbock.com", "T9jqcxx#", "Fgp14159");
    }

    public static List<User> fromCsv(String filename) {
        return User.getByLists(new CSVReader(filename).getAllCells());
    }

    public static List<User> fromCsv() {
        return fromCsv("users.csv");
    }

}
