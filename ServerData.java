import java.io.*;

public class ServerData {

    // Once object created, ip and port should not change; final
    private final String server_ip;
    private final int server_port;

    ServerData(String server_ip, int server_port) {
        this.server_ip = server_ip;
        this.server_port = server_port;
    }

    // Getter method
    public String getIp() {
        return server_ip;
    }

    public int getPort() {
        return server_port;
    }

    // Load ServerData object from file to get the values
    public static ServerData loadFromFile(String filePath) {
        String tmp_ip = null;
        int tmp_port = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("server_ip")) {
                    tmp_ip = line.split(":")[1].trim();
                } else if (line.startsWith("server_port")) {
                    tmp_port = Integer.parseInt(line.split(":")[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new ServerData(tmp_ip, tmp_port);
    }


    // Main method for test
    public static void main(String[] args) {
        String filePath = "c:/server_info.dat";

        ServerData serverData = ServerData.loadFromFile(filePath);

        // ServerData == null mean that there is a thrown IOException
        if (serverData != null) {
            System.out.println("Server IP: " + serverData.getIp());
            System.out.println("Server Port: " + serverData.getPort());
        }
    }
}
