package cse.oop.sgudmunson.hw9.one;

/**
 * Thread to connect to the server and send/recieve messages
 *
 *
 * @author Shadron Gudmunson
 * @version HW <9>, #<1>
 * @bugs none
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import static cse.oop.sgudmunson.hw9.one.ClientFrame.chat;

/**
 * Created by ritakuo on 4/9/19.
 */
public class ServerThread implements Runnable {
    private Socket client;
    private String name;
    private final LinkedList<String> messages;
    
    /**
     * sends a new message to the server
     * @param new_message the new message
     */
    public void newMessage(String new_message) {
        synchronized (messages) {
            messages.push(new_message);
        }
    }
    
    /**
     * creates a server thread with a connection to client
     * @param client socket to the client
     * @param name user name of client
     */
    public ServerThread(Socket client, String name) {
        this.client = client;
        this.name = name;
        messages = new LinkedList<String>();
    }
    
    /**
     * handles sending and recieving messages to/from server
     */
    public void run() {
        System.out.println("Welcome " + name);
        try {
            PrintWriter out_stream = new PrintWriter(client.getOutputStream());
            InputStream in_stream = client.getInputStream();
            Scanner in = new Scanner(in_stream);

            while (!client.isClosed()) {
                // Get input from the client socket
                if (in_stream.available() > 0) {
                    if (in.hasNextLine()) {
                        chat.append(in.nextLine() + "\n");
                    }
                }

                // Send message to the socket server
                if (!messages.isEmpty()) {
                    String next = null;
                    synchronized (messages) {
                        next = messages.pop();
                    }
                    out_stream.println(next);
                    out_stream.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
