package util;

import java.io.BufferedReader;
import java.io.IOException;

public class Utils {

    private Utils(){}

    public static String getHtmlFileName(BufferedReader requestUrl) throws IOException {
        return requestUrl.readLine().split(" ")[1];
    }
}
