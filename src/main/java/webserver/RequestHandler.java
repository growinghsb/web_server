package webserver;

import database.UserDB;
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
            return requestGET(requestMessages[1], url);
        }

        if (requestMessages[0].equals("POST")) {
            return requestPOST(requestMessages[1], url);
        }
        return null;
    }

    private byte[] requestGET(String requestUrl, BufferedReader httpRequest) throws IOException {
        if (requestUrl.contains("user/userList")) {
            if (isLogin(httpRequest)) {
                UserDB.getUsers();
                return getFilePath(requestUrl);
            }
            return getFilePath("user/login");
        }
        return getFilePath(requestUrl);
    }

    private boolean isLogin(BufferedReader httpRequest) throws IOException {
        return Utils.getCookieValue(httpRequest).contains("true");
    }

    private byte[] requestPOST(String requestUrl, BufferedReader resource) throws IOException {

        if (isSign(requestUrl)) {
            Sign.sign(getBodyContent(resource));
            return getFilePath("user/login");
        }

        if (isLogin(requestUrl)) {
            if (Login.login(getBodyContent(resource))) {
                Response.setIsCookie(true);
                return getFilePath("/");
            }
            Response.setIsCookie(false);
            return getFilePath("user/login");
        }
        return getFilePath(requestUrl);
    }

    private byte[] getFilePath(String requestUrl) throws IOException {
        return Utils.getHtmlFilePath(requestUrl);
    }

    private String getBodyContent(BufferedReader resource) throws IOException {
        return Utils.getHttpBodyContent(resource);
    }

    private boolean isLogin(String requestUrl) {
        return requestUrl.contains("/");
    }

    private boolean isSign(String requestUrl) {
        return requestUrl.contains("login");
    }
}