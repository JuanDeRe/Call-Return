package Ex6;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayDeque;
import java.util.Queue;

public class MessengerServerImpl implements MessengerServer{

    private Queue<Message> messages = new ArrayDeque<>();
    private int idCounter = 0;

    public MessengerServerImpl(String ipRMIregistry, int portRMIregistry, String publicationName){
        try {
            MessengerServer messengerServer = (MessengerServer) UnicastRemoteObject.exportObject(this,0);
            LocateRegistry.createRegistry(23000);
            Registry registry = LocateRegistry.getRegistry(ipRMIregistry,portRMIregistry);
            registry.rebind(publicationName, messengerServer);
            System.out.println("Messenger server ready...");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(String text, String sender) throws RemoteException {
        idCounter ++;
        Message message = new Message(idCounter, sender,text);
        messages.add(message);
    }

    @Override
    public Queue<Message> checkNewMessages(int lastMessage) throws RemoteException{
        Queue<Message> newMessages = new ArrayDeque<>();
        for (Message message : messages){
            if (message.getId() > lastMessage){
                newMessages.add(message);
            }
        }
        return newMessages;
    }

    public static void main(String[] args){
        new MessengerServerImpl("127.0.0.1", 23000, "messengerServer");
    }
}
