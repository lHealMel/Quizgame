import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HwServer {
    private static final int PORT = 8888;

    // To determine which threads are
    private static final AtomicInteger clientCounter = new AtomicInteger(0);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientId = clientCounter.incrementAndGet();

                // Create client handling thread
                Thread thread = new Thread(new ClientHandler(clientSocket, clientId));
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}