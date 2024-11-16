import java.io.*;
import java.net.*;

public class HwClient {
    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedReader stin = null;
        BufferedWriter out = null;

        //get the server info
        String filePath = "c:/server_info.dat";
        String ip = null;
        int port = 0;

        ServerData serverData = ServerData.loadFromFile(filePath);
        if (serverData != null) {
            ip = serverData.getIp();
            port = serverData.getPort();
        } else { // If ServerData.loadFromFile(filePath) returns null, set to fixed value
            System.out.println("Cannot get the server info from file.\n 127.0.0.1/1234 will be used");
            ip = "127.0.0.1";
            port = 1234;
        }

        try (Socket socket = new Socket(ip, port)) {
            System.out.println("Connected to server: " + ip + ":" + port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stin = new BufferedReader(new InputStreamReader(System.in));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Send a message to server that connect, to get the response from server
            out.write("connect\n");
            out.flush();

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {

                System.out.println(serverMessage);

                if (serverMessage.toLowerCase().contains("quiz complete")) {
                    break;
                }

                // If server message is problem, get the answer from console
                if (serverMessage.startsWith("Server>")) { // 문제 메시지인지 확인
                    System.out.print("Your answer: ");
                    String answer = stin.readLine();
                    out.write(answer + "\n");
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
