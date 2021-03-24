package database;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB {

    private static List<User> users = new ArrayList<>();

    private UserDB() {}

    public static void insertUser(User user) {
        users.add(user);
    }

    public static int usersCount() {
        return users.size();
    }
}
