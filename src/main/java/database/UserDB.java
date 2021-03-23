package database;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class UserDB {

    private static Map<String, User> users = new HashMap<>();

    public static void insertUser(User user) {
        users.put(user.getId(), user);
    }

    public static int usersCount() {
        return users.size();
    }
}
