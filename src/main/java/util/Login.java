package util;

import database.UserDB;

public class Login {

    public static boolean login(String loginParameter) {
        return getParameter(loginParameter);
    }

    private static boolean getParameter(String loginParameter) {

        String[] userInfos = loginParameter.split("&");
        String[] userValues = new String[userInfos.length];

        for (int i = 0; i < userValues.length; i++) {
            userValues[i] = userInfos[i].split("=")[1];
        }

        return UserDB.checkLogin(userValues[0], userValues[1]);
    }
}
