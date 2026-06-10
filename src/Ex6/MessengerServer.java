package Ex6;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Queue;

public interface MessengerServer extends Remote {
    void sendMessage(String text, String sender) throws RemoteException;

    Queue<Message> checkNewMessages(int lastMessage) throws RemoteException;
}
