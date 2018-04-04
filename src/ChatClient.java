import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements Runnable {

    private Socket TCPSocket = null;
    private DatagramSocket UDPSocket = null;
    private DataOutputStream out;
    private DataInputStream in;
    private String hostName;
    private int portNumber;
    private Thread TCPListener;
    private Thread UDPListener;

    public static void main(String args[]) {
        if(args.length == 2) {
            String hostName = args[0];
            int portNumber = Integer.parseInt(args[1]);
            new ChatClient(hostName, portNumber);
        } else {
            throw new IllegalArgumentException("Invalid number of parameters!");
        }

    }

    public ChatClient(String hostName, int portNumber) {

        this.hostName = hostName;
        this.portNumber = portNumber;

        try {
            // create socket
            TCPSocket = new Socket(hostName, portNumber);
            UDPSocket = new DatagramSocket();

            // in & out streams
            out = new DataOutputStream(new BufferedOutputStream(TCPSocket.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(TCPSocket.getInputStream()));

            TCPListener = new Thread(this);
            TCPListener.start();

            UDPListener = new Thread(new ClientUDPListener(TCPSocket.getLocalPort()));
            UDPListener.start();

            // send msg
            startChat();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (TCPSocket != null){
                try {
                    TCPSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startChat() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your nickname:");
        String nickname = scanner.next();
        System.out.println("You can chat now!");

        while(true) {
            String msg = scanner.next();
            try {
                if(msg.equals("U")) {
                    InetAddress address = InetAddress.getByName(hostName);
                    msg = nickname+": "+scanner.next();
                    byte[] sendBuffer = msg.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
                    UDPSocket.send(sendPacket);
                } else {
                    out.writeUTF(nickname +": "+msg);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                String line = in.readUTF();
                System.out.println(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            TCPListener = null;
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
