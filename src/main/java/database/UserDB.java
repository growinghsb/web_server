package database;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB {

    private static List<User> users = new ArrayList<>();

    private UserDB() {
    }

    public static StringBuilder getUsers() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>");
        for (User user : users) {
            sb.append("유저ID : " + user.getId()).append(" | 유저이름 : " + user.getName()).append(" | 이메일 : " + user.getEmail());
        }
        sb.append("</h3>");
        return sb;
    }

    public static void insertUser(User user) {
        users.add(user);
    }

    public static boolean checkLogin(String inputId, String inputPassword) {
        return validation(inputId, inputPassword);
    }

    private static boolean validation(String inputId, String inputPassword) {
        for (User user : users) {
            if (isId(user.getId(), inputId)) {
                if (isPassword(user.getPassword(), inputPassword)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isId(String userId, String inputId) {
        return userId.equals(inputId);
    }

    private static boolean isPassword(String userPassword, String inputPassword) {
        return userPassword.equals(inputPassword);
    }
}
