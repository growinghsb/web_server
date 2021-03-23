package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            byte[] body = createResponseBody(br);

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
        String htmlFileUrl = splitUrl(url.readLine());
        System.out.println(htmlFileUrl + "첫 줄");
        while (url.ready()) {
            System.out.println(url.readLine());
        }

        if (htmlFileUrl.equals("/")) {
            return readHtmlFile("index");
        }

        return readHtmlFile(htmlFileUrl);

    }

    private String splitUrl(String url) {
        return url.split(" ")[1];
    }

    private byte[] readHtmlFile(String htmlFileName) throws IOException {
        if (htmlFileName.contains("?")) {
            htmlFileName = htmlFileName.replace("?", "");
        }
        return Files.readAllBytes(new File("./src/main/resources/static/" + htmlFileName + ".html").toPath());
    }
}