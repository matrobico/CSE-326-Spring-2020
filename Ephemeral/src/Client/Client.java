package cse.oop.sgudmunson.hw9.one;

/**
 * Is the backend for a chat client
 *
 *
 * @author Shadron Gudmunson
 * @version HW <9>, #<1>
 * @bugs none
 */

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static cse.oop.sgudmunson.hw9.one.ClientFrame.chat;

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
            // Establish a connection with the server
            Socket client = new Socket("eph.nopesled.com", 443);
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
