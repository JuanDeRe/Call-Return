package Ex3;
import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket;
        Socket clientSocket;
        try{
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        String outputLine;

        while ((inputLine = in.readLine()) != null){
            inputLine = inputLine.trim();
            if(inputLine.equals("fin")){
                break;
            }
            System.out.println("Procesando operación: " +inputLine+"^2");
            outputLine = calculateSquare(Integer.parseInt(inputLine)).toString();
            out.println("Resultado: "+ outputLine);
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

    private static Integer calculateSquare(Integer n){
        return n*n;
    }
}
