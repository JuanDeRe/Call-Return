package Ex3_2;
import java.net.*;
import java.io.*;
import javax.script.ScriptException;

public class Server {
    public static void main(String[] args) throws IOException, ScriptException {
        ServerSocket serverSocket;
        Socket clientSocket;
        try{
            serverSocket = new ServerSocket(5001);
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
        String function = "cos";

        while ((inputLine = in.readLine()) != null){
            inputLine = inputLine.trim();
            if(inputLine.equals("fin")){
                break;
            } else if (inputLine.startsWith("fun:")) {
                function = inputLine.split(":")[1];
                out.println("Función cambiada a : "+function);
                continue;
            }

            System.out.println("Procesando operación: " +function+"("+inputLine+")");
            outputLine = String.valueOf(calculateFunction(inputLine, function));
            out.println("Resultado: "+ outputLine);
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

    private static double calculateFunction(String n, String function) {
        String input = n.trim().toLowerCase().replace("pi", String.valueOf(Math.PI));
        double value = parseValue(input);
        return switch (function.toLowerCase()) {
            case "cos" -> fixPrecision(Math.cos(value));
            case "sin" -> fixPrecision(Math.sin(value));
            case "tan" -> fixPrecision(Math.tan(value));
            default -> 0;
        };
    }
    private static double parseValue(String input) {
        String[] parts = input.split("/");

        if (parts.length == 2) {
            double numerator = Double.parseDouble(parts[0]);
            double denominator = Double.parseDouble(parts[1]);
            return numerator / denominator;
        }

        return Double.parseDouble(input);
    }
    private static double fixPrecision(double value) {
        if (Math.abs(value) < 1e-10) {
            return 0;
        }
        return value;
    }
}
