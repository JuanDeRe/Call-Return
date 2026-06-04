package Ex2;

import java.io.IOException;
import java.net.*;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class URLReader {
    public static void main(String[] args) {
        URL url = readUrl();
        saveUrlInFile(url);
    }

    private static void saveUrlInFile(URL url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))){
            FileWriter fw = new FileWriter("html/resultado.html");
            String inputLine;
            while ((inputLine = reader.readLine()) != null){
                fw.write(inputLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static URL readUrl() {
        String url = System.console().readLine("Digite una URL: ");
        try {
            return new URL(url.trim());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
