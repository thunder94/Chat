import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    public ChatServer(int portNumber) {

        ServerSocket serverTCPSocket = null;

        try {
            // create socket
            serverTCPSocket = new ServerSocket(portNumber);

            while(true){
                // accept client
                Socket clientSocket = serverTCPSocket.accept();
                System.out.println("client connected");

                ChatHandler c = new ChatHandler(clientSocket);
                c.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (serverTCPSocket != null){
                try {
                    serverTCPSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String args[]) {
        if(args.length != 1) {
            throw new IllegalArgumentException("Provide port number as parameter!");
        }
        try {
            int portNumber = Integer.parseInt(args[0]);
            new ChatServer(portNumber);
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
