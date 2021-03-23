package webserver;

import database.UserDB;
import jdk.jshell.execution.Util;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class RequestHandler extends Thread {

    private static final Logger log =
            LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port {}",
                connection.getInetAddress(), connection.getPort());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
             OutputStream out = connection.getOutputStream()) {

            createResponse(new DataOutputStream(out), createResponseBody(in));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void createResponse(DataOutputStream dos, byte[] body) {
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length:" + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] createResponseBody(BufferedReader url) throws IOException {
        String htmlFileName = parameterExtraction(Utils.getHtmlFileName(url));

        if (htmlFileName.equals("/")) {
            return Utils.getHtmlFilePath("index");
        }

        return Utils.getHtmlFilePath(htmlFileName);
    }

    private String parameterExtraction(String requestUrl) {
        if (requestUrl.contains("?")) {
            String[] token = requestUrl.replace("?", " ").split(" ");
            userSave(token[token.length - 1]);
            requestUrl = token[0];
        }

        return requestUrl;
    }

    private void userSave(String userParameter) {
        if (!userParameter.contains("/")) {
            String[] userInfo = userParameter.split("&");
            String[] userValue = new String[userInfo.length];

            for (int i = 0; i < userValue.length; i++) {
                userValue[i] = userInfo[i].split("=")[1];
            }

            UserDB.insertUser(new User(userValue[0], userValue[1], userValue[2], userValue[3]));
        }
    }
}