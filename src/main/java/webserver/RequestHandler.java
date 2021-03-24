package webserver;

import database.UserDB;
import jdk.jshell.execution.Util;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Response;
import util.Utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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

            Response.createResponse(new DataOutputStream(out), requestHandler(in));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] requestHandler(BufferedReader url) throws IOException {
        String[] requestMessages = Utils.getRequestHttpHeaderMessages(url);

        if (requestMessages[0].equals("GET")) {
            return requestGET(requestMessages);
        }

        if (requestMessages[0].equals("POST")) {
            return requestPOST(requestMessages, url);
        }
        return null;
    }

    private byte[] requestGET(String[] requestMessage) throws IOException {
        return Utils.getHtmlFilePath(requestMessage[1]);
    }

    private byte[] requestPOST(String[] requestMessage, BufferedReader resource) throws IOException {
        getRequestHttpBody(resource);
        return Utils.getHtmlFilePath(requestMessage[1]);
    }

    private void getRequestHttpBody(BufferedReader resource) throws IOException {
        userSave(Utils.getHttpBodyContent(resource));
    }


    private void userSave(String userParameter) {

        String[] userInfos = userParameter.split("&");
        String[] userValues = new String[userInfos.length];

        for (int i = 0; i < userValues.length; i++) {
            userValues[i] = userInfos[i].split("=")[1];
        }

        UserDB.insertUser(User.createUser(userValues));
    }
}