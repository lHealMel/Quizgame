import java.io.*;
import java.net.*;


// Client handler for multi thread
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final int clientId;

    private static final String[] problems = {
            "1. What is 1 + 1?",
            "2. What is 5 * 5?",
            "3. What is 5 * 16?"
    };
    private static final String[] answers = {"2", "25", "80"};

    private BufferedReader in;
    private BufferedWriter out;
    private int grade;

    public ClientHandler(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }

    // Since use thread, override run method
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String clientMessage = in.readLine();

            if ("connect".equalsIgnoreCase(clientMessage)) {
                System.out.println("Client " + clientId + " connected: " + socket.getInetAddress());
                sendProblem(0);
            }

            // Determine which number of problem to send
            int problemIndex = 0;
            while (true) {
                String answer = in.readLine();
                if (answer == null) break;

                System.out.println("Client " + clientId + "'s answer to question " + (problemIndex + 1) + ": " + answer);
                if (answers[problemIndex].equals(answer)) {
                    grade += 1;
                    sendMessage("Correct!");
                } else {
                    sendMessage("Incorrect.");
                }

                problemIndex++;
                if (problemIndex < problems.length) {
                    sendProblem(problemIndex);
                } else {
                    sendMessage("Your score : " + grade + " on " + problems.length+ "\nQuiz completed.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                socket.close();
                System.out.println("Connection from " + clientId + " closed.");
            } catch (IOException e) {
                System.out.println("Error closing connection.");
            }
        }
    }

    // Method for send a problem
    private void sendProblem(int index) throws IOException {
        sendMessage("Server> " + problems[index]);
    }

    // Method for send a message from server
    private void sendMessage(String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }
}