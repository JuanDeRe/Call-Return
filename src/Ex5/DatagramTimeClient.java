package Ex5;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class DatagramTimeClient {

    public static void main(String[] args) throws InterruptedException {
        String lastReceived = "Todavía no se ha recibido hora del servidor";
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2000);
            InetAddress address = InetAddress.getByName("127.0.0.1");
            while (true) {
                try {
                    byte[] sendBuffer = "time".getBytes(StandardCharsets.UTF_8);
                    DatagramPacket requestPacket = new DatagramPacket(
                            sendBuffer,
                            sendBuffer.length,
                            address,
                            5500
                    );
                    socket.send(requestPacket);
                    byte[] receiveBuffer = new byte[256];

                    DatagramPacket responsePacket = new DatagramPacket(
                            receiveBuffer,
                            receiveBuffer.length
                    );
                    socket.receive(responsePacket);
                    lastReceived = new String(
                            responsePacket.getData(),
                            0,
                            responsePacket.getLength(),
                            StandardCharsets.UTF_8
                    );
                    System.out.println("Servidor activo. Date: " + lastReceived);
                } catch (SocketTimeoutException e) {
                    System.out.println("Servidor no responde. Última hora conocida: " + lastReceived);

                } catch (IOException e) {
                    System.out.println("Error de comunicación. Última hora conocida: " + lastReceived);
                }
                Thread.sleep(5000);
            }
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}