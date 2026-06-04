package Ex1;

import java.io.*;

public class URL {
    public static void main(String[] args) throws IOException {
        java.net.URL youtube = new java.net.URL("https://www.youtube.com:443/watch?v=tyAYwGAyulM");
        System.out.println("Protocolo: " + youtube.getProtocol());
        System.out.println("Autoridad: " +youtube.getAuthority());
        System.out.println("Host: " +youtube.getHost());
        System.out.println("Puerto: " +youtube.getPort());
        System.out.println("Path: " +youtube.getPath());
        System.out.println("Query: " +youtube.getQuery());
        System.out.println("File: " +youtube.getFile());
        System.out.println("Referencia: " +youtube.getRef());
    }
}
