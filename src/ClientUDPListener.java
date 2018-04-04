import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class ClientUDPListener implements Runnable{

    private DatagramSocket socket = null;

    public ClientUDPListener(int portNumber) {

        try{
            socket = new DatagramSocket(portNumber);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[1024];

        try {
            while(true) {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                System.out.println(msg.toString());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }

    }

}
