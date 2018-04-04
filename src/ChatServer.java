import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatServer {

    private DatagramSocket UDPSocket;
    private List<Integer> clientPorts = new ArrayList<>();

    public ChatServer(int portNumber) {

        ServerSocket serverTCPSocket = null;

        try {
            // create socket
            serverTCPSocket = new ServerSocket(portNumber);
            UDPSocket = new DatagramSocket(portNumber);

            while(true){
                // accept client
                Socket clientSocket = serverTCPSocket.accept();
                clientPorts.add(clientSocket.getPort());
                System.out.println("client connected");

                ChatHandler c = new ChatHandler(clientSocket);
                c.start();

                handleUDP();
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

    private void handleUDP() {
        Thread UDPListener = new Thread(() -> {
            byte[] receiveBuffer = new byte[1024];

            try {
                while(true) {
                    Arrays.fill(receiveBuffer, (byte)0);
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    UDPSocket.receive(receivePacket);
                    String msg = new String(receivePacket.getData());
                    for(Integer port: clientPorts) {
                        if(receivePacket.getPort() != port) {
                            System.out.println("getPort: "+receivePacket.getPort());
                            System.out.println("port: "+port);
                            InetAddress address = receivePacket.getAddress();
                            byte[] sendBuffer = msg.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
                            UDPSocket.send(sendPacket);
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if (UDPSocket != null) {
                    UDPSocket.close();
                }
            }
        });
        UDPListener.start();
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
