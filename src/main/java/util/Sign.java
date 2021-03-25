package util;

import database.UserDB;
import model.User;

public class Sign {

    public static void sign(String userParameter) {
        userSave(userParameter);
    }

    private static void userSave(String userParameter) {

        String[] userInfos = userParameter.split("&");
        String[] userValues = new String[userInfos.length];

        for (int i = 0; i < userValues.length; i++) {
            userValues[i] = userInfos[i].split("=")[1];
        }

        UserDB.insertUser(User.createUser(userValues));
    }

}
