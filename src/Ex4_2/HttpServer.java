package Ex4_2;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HttpServer {

    private static final AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(45000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 45000");
            System.exit(1);
        }

        System.out.println("Listo para recibir...");
        System.out.println("Escribe 'fin' en la consola para apagar el servidor.");

        startConsoleListener(serverSocket);

        listen(serverSocket);

        System.out.println("Servidor apagado.");
    }

    private static void startConsoleListener(ServerSocket serverSocket) {
        Thread consoleThread = new Thread(() -> {
            try {
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                String line;

                while ((line = console.readLine()) != null) {
                    if (line.equalsIgnoreCase("fin")) {
                        System.out.println("Apagando servidor...");
                        running.set(false);

                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        consoleThread.start();
    }

    private static void listen(ServerSocket serverSocket) {
        while (running.get()) {
            try {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            } catch (SocketException e) {
                if (!running.get()) {
                    break;
                }
                System.err.println("Socket error: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Accept failed.");
                e.printStackTrace();
            }
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
                OutputStream out = clientSocket.getOutputStream()
        ) {
            String requestLine = in.readLine();

            if (requestLine == null) {
                return;
            }

            System.out.println("Request: " + requestLine);

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Header: " + inputLine);

                if (inputLine.isEmpty()) {
                    break;
                }
            }

            String path = getPathFromRequest(requestLine);

            route(path, out);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getPathFromRequest(String requestLine) {

        String[] parts = requestLine.split(" ");

        if (parts.length >= 2) {
            return parts[1];
        }

        return "/";
    }

    private static void route(String path, OutputStream out) throws IOException {
        if (path.equals("/")) {
            String html = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Welcome</title>"
                    + "</head>"
                    + "<body>"
                    + "<h1>Home</h1>"
                    + "<a href=\"/page2\">Go to page 2</a>"
                    + "<br>"
                    + "<a href=\"/images/1\">Image 1</a>"
                    + "</body>"
                    + "</html>";

            sendTextResponse(out, "200 OK", "text/html; charset=UTF-8", html);

        } else if (path.equals("/page2")) {
            String html = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Page 2</title>"
                    + "</head>"
                    + "<body>"
                    + "<p>:P</p>"
                    + "<a href=\"/\">Home</a>"
                    + "</body>"
                    + "</html>";

            sendTextResponse(out, "200 OK", "text/html; charset=UTF-8", html);

        } else if (path.equals("/images/1")) {
            sendImage(out, "images/ex1.png", "image/png");

        } else {
            String html = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>404</title>"
                    + "</head>"
                    + "<body>"
                    + "<h1>404 - Not found</h1>"
                    + "<a href=\"/\">Home</a>"
                    + "</body>"
                    + "</html>";

            sendTextResponse(out, "404 Not Found", "text/html; charset=UTF-8", html);
        }
    }

    private static void sendTextResponse(
            OutputStream out,
            String status,
            String contentType,
            String body
    ) throws IOException {
        byte[] bodyBytes = body.getBytes("UTF-8");

        String headers = "HTTP/1.1 " + status + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + bodyBytes.length + "\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        out.write(headers.getBytes("UTF-8"));
        out.write(bodyBytes);
        out.flush();
    }

    private static void sendImage(
            OutputStream out,
            String imagePath,
            String contentType
    ) throws IOException {
        File file = new File(imagePath);

        if (!file.exists()) {
            String html = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head><meta charset=\"UTF-8\"><title>Image not found</title></head>"
                    + "<body>"
                    + "<h1>404 - Image not found</h1>"
                    + "<a href=\"/\">Home</a>"
                    + "</body>"
                    + "</html>";

            sendTextResponse(out, "404 Not Found", "text/html; charset=UTF-8", html);
            return;
        }

        byte[] imageBytes = Files.readAllBytes(file.toPath());

        String headers = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + imageBytes.length + "\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        out.write(headers.getBytes("UTF-8"));
        out.write(imageBytes);
        out.flush();
    }
}