package util;

import database.UserDB;

public class Login {

    public static void login(String loginParameter) {
        getParameter(loginParameter);
    }

    private static void getParameter(String loginParameter) {

        String[] userInfos = loginParameter.split("&");
        String[] userValues = new String[userInfos.length];

        for (int i = 0; i < userValues.length; i++) {
            userValues[i] = userInfos[i].split("=")[1];
        }

        UserDB.checkLogin(userValues[0], userValues[1]);
    }
}
