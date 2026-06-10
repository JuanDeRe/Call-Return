package Ex6;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MessengerClient {

    public void ejecutaServicio(String ipRmiregistry, int puertoRmiRegistry, String nombreServicio) {
        try {
            Registry registry = LocateRegistry.getRegistry(ipRmiregistry, puertoRmiRegistry);

            MessengerServer messengerServer = (MessengerServer) registry.lookup(nombreServicio);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Escribe tu nombre de usuario: ");
            String username = scanner.nextLine();

            AtomicBoolean running = new AtomicBoolean(true);
            AtomicInteger lastMessage = new AtomicInteger(0);

            Thread receiveThread = new Thread(() -> {
               while (running.get()){
                   try {
                       Queue<Message> newMessages = messengerServer.checkNewMessages(lastMessage.get());
                       for (Message message : newMessages){
                           if (!message.getSender().equals(username)){
                               System.out.println("\n" + message.getSender() + ": " + message.getText());
                               System.out.print("> ");
                           }
                           lastMessage.set(message.getId());
                       }
                   } catch (RemoteException e) {
                       throw new RuntimeException(e);
                   }
               }
            });
            receiveThread.start();
            System.out.println("Escribe 'fin' para salir...");
            System.out.print("> ");

            String message = "";

            while (!message.equals("fin")){
                message = scanner.nextLine();
                if (!message.equals("fin")){
                    messengerServer.sendMessage(message,username);
                }
                System.out.print("> ");
            }
            running.set(false);

        } catch (Exception e) {
            System.err.println("Hay un problema:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MessengerClient ec = new MessengerClient();
        ec.ejecutaServicio("127.0.0.1", 23000, "messengerServer");
    }
}