import java.io.*;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements Runnable {

    private Socket TCPSocket = null;
    private DatagramSocket UDPSocket = null;
    private DataOutputStream out;
    private DataInputStream in;
    private Thread listener;

    public static void main(String args[]) {
        if(args.length == 2) {
            String hostName = args[0];
            int portNumber = Integer.parseInt(args[1]);
            new ChatClient(hostName, portNumber);
        } else if(args.length == 3) {

        } else throw new IllegalArgumentException("Invalid number of parameters!");

    }

    public ChatClient(String hostName, int portNumber) {

        try {
            // create socket
            TCPSocket = new Socket(hostName, portNumber);
            UDPSocket = new DatagramSocket();

            // in & out streams
            out = new DataOutputStream(new BufferedOutputStream(TCPSocket.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(TCPSocket.getInputStream()));

            listener = new Thread(this);
            listener.start();

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
        while(true) {
            String msg = scanner.next();
            try {
                if(msg.equals("U")) {

                } else {
                    out.writeUTF(msg);
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
                System.out.println("received response: " + line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            listener = null;
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
