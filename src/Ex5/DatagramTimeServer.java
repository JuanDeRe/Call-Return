package Ex5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class DatagramTimeServer {

    DatagramSocket socket;

    public DatagramTimeServer(){
        try{
            socket = new DatagramSocket(5500);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer(){
        byte[] buf = new byte[256];
        while(true){
            try{
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String dString = new Date().toString();
                buf = dString.getBytes();
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //socket.close();
    }

    public static void main(String[] args){
        DatagramTimeServer ds = new DatagramTimeServer();
        ds.startServer();
    }
}
