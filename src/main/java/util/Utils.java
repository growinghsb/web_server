package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private Utils() {
    }

    public static String[] getRequestHttpHeaderMessages(BufferedReader requestUrl) throws IOException {
        return requestUrl.readLine().split(" ");
    }

    public static byte[] getHtmlFilePath(String resource) throws IOException {
        return Files.readAllBytes(new File("./src/main/resources/static/" + changeHtmlFileName(validation(resource)) + ".html").toPath());
    }

    private static String validation(String resource) {
        if (resource.contains("?")) {
            return resource.replace("?", "");
        }
        return resource;
    }

    private static String changeHtmlFileName(String resource) {
        if (resource.equals("/")) {
            return "index";
        }
        return resource;
    }

    public static String getHttpBodyContent(BufferedReader httpHeader) throws IOException {
        return getContent(getContentLength(httpHeader), httpHeader);

    }

    private static int getContentLength(BufferedReader httpRequestMessage) throws IOException {
        String requestHeaderMessage = "";

        String contentLength = "";
        while (httpRequestMessage.ready()) {
            requestHeaderMessage = httpRequestMessage.readLine();

            if (requestHeaderMessage.contains("Content-Length:")) {
                contentLength = requestHeaderMessage;
                break;
            }

        }
        return Integer.parseInt(contentLength.split(" ")[1]);
    }

    public static String getCookieValue(BufferedReader httpHeader) throws IOException {
        String cookie = "";
        while (httpHeader.ready()) {
            cookie = httpHeader.readLine();
            if (cookie.contains("Cookie:")) {
                break;
            }
        }

        return cookie;
    }

    private static String getContent(int contentLength, BufferedReader httpHeader) throws IOException {
        List<Character> content = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        while (httpHeader.ready()) {
            content.add((char) httpHeader.read());
        }

        content = content.subList(content.size() - contentLength, content.size());

        for (Character character : content) {
            sb.append(character);
        }

        return sb.toString();
    }
}
