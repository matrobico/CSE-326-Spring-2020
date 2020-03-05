package Client;

/**
 * Is the backend for a chat client
 *
 *
 * @author Shadron Gudmunson
 * @version HW <9>, #<1>
 * @bugs none
 */

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by ritakuo on 4/9/19.
 */
public class Client {
    private int port;
    private String name;
    private ServerThread server_thread;
    
    /**
     * gets the server thread for the client
     * @return the value of the server thread
     */
    public ServerThread getServer_thread() {
        return server_thread;
    }
    
    /**
     * creates a client connect to server at port and sets username to name
     * @param port port of the server
     * @param name username of the server
     */
    public Client(int port, String name) {
        this.port = port;
        this.name = name;
        startClient();
    }

    private void startClient() {
        try {
            /*
            // Establish a connection with the server
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket client = (SSLSocket) sslsocketfactory.createSocket("192.168.1.10", 3000);
            client.startHandshake();

            client.addHandshakeCompletedListener(
                    new HandshakeCompletedListener() {
                        public void handshakeCompleted(
                                HandshakeCompletedEvent event) {
                            System.out.println("Handshake finished!");
                            System.out.println(
                                    "\t CipherSuite:" + event.getCipherSuite());
                            System.out.println(
                                    "\t SessionId " + event.getSession());
                            System.out.println(
                                    "\t PeerHost " + event.getSession().getPeerHost());
                        }
                    }
            );
            */
            Socket client = new Socket("192.168.1.10", 3000);
            Thread.sleep(1000);

            // Create a thread to receive the messages from the server
            server_thread = new ServerThread(client, name);
            Thread server = new Thread(server_thread);
            server.start();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
