package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Utils {

    private Utils(){}

    public static String getHtmlFileName(BufferedReader requestUrl) throws IOException {
        return requestUrl.readLine().split(" ")[1];
    }

    public static byte[] getHtmlFilePath(String resource) throws IOException {
        return Files.readAllBytes(new File("./src/main/resources/static/" + validation(resource) + ".html").toPath());
    }

    private static String validation(String resource) {
        if (resource.contains("?")) {
            return resource.replace("?", "");
        }
        return resource;
    }
}
