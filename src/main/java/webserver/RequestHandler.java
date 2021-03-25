package webserver;

import database.UserDB;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Login;
import util.Response;
import util.Sign;
import util.Utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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

        try (BufferedReader in =
                     new BufferedReader(
                             new InputStreamReader(connection.getInputStream(),
                                     StandardCharsets.UTF_8));
             OutputStream out = connection.getOutputStream()) {

            Response.createResponse(new DataOutputStream(out), requestHandler(in));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] requestHandler(BufferedReader url) throws IOException {
        String[] requestMessages = Utils.getRequestHttpHeaderMessages(url);

        if (requestMessages[0].equals("GET")) {
            return requestGET(requestMessages[1]);
        }

        if (requestMessages[0].equals("POST")) {
            return requestPOST(requestMessages[1], url);
        }
        return null;
    }

    private byte[] requestGET(String requestUrl) throws IOException {
        return getFilePath(requestUrl);
    }

    private byte[] requestPOST(String requestUrl, BufferedReader resource) throws IOException {
        String responseUrl = requestUrl;

        if (isSign(requestUrl)) {
            Sign.sign(getBodyContent(resource));
            responseUrl = "login";
        }

        if (isLogin(requestUrl)) {
            Login.login(getBodyContent(resource));
            responseUrl = "index";
        }

        return getFilePath(responseUrl);
    }

    private byte[] getFilePath(String requestUrl) throws IOException {
        return Utils.getHtmlFilePath(requestUrl);
    }

    private String getBodyContent(BufferedReader resource) throws IOException {
        return Utils.getHttpBodyContent(resource);
    }

    private boolean isLogin(String requestUrl) {
        return requestUrl.contains("login");
    }

    private boolean isSign(String requestUrl) {
        return requestUrl.contains("sign");
    }
}