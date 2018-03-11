import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatHandler extends Thread{

    private Socket TCPSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private static List<ChatHandler> handlers = new ArrayList<>();

    public ChatHandler(Socket TCPSocket) throws IOException{
        this.TCPSocket = TCPSocket;
        this.in = new DataInputStream(new BufferedInputStream(TCPSocket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(TCPSocket.getOutputStream()));
    }

    public void run () {
        try {
            handlers.add(this);
            while (true) {
                String msg = in.readUTF ();
                broadcast(msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace ();
        } finally {
            handlers.remove(this);
            try {
                TCPSocket.close ();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void broadcast (String message) {
        synchronized (handlers) {
            for(ChatHandler handler: handlers) {
                if(handler != this) {
                    try {
                        synchronized (handler.out) {
                            handler.out.writeUTF (message);
                        }
                        handler.out.flush ();
                    } catch (IOException ex) {
                        handler.interrupt();
                    }
                }
            }
        }
    }

}
