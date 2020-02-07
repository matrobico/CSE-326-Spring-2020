package cse.oop.sgudmunson.hw9.two;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * threads created on a server for a chat client
 *
 *
 * @author Shadron Gudmunson
 * @version HW <9>, #<2>
 * @bugs Does not detect users leaving correctly
 */

/**
 * Created by ritakuo on 4/9/19.
 */
public class ClientThread implements Runnable {
    private Server server;
    private Socket client;
    private PrintWriter out;
    private InputStream inStream;
    public String name;
    private boolean isClosed;
    
    /**
     * sets the isClosed attribute
     * @param closed
     */
    public void setClosed(boolean closed) {
        isClosed = closed;
    }
    
    /**
     * gets the isClosed variable
     * @return value of isClosed
     */
    public boolean getClosed(){
        return isClosed;
    }
    
    /**
     * gets the name attribute
     * @return the name of the clientThread
     */
    public String getName() {
        //System.out.println("in get " + name);
        return name;
        
    }
    
    /**
     * sets the name attribute
     * @param name value to set name to
     */
    public void setName(String name) {
        //System.out.println("in set " + name);
        this.name = name;
    }
    
    /**
     * gets the clients inStream
     * @return clients inStream
     */
    public InputStream getInStream() {
        return inStream;
    }
    
    /**
     * gets the writer of the client
     * @return the clients writer
     */
    public PrintWriter getWriter() {
        return out;
    }
    
    /**
     * Creates a clientThread with server and client
     * @param server Socket of the server to communicate with
     * @param client Socket of the client to communicate with
     */
    public ClientThread(Server server, Socket client) {
        this.server = server;
        this.client = client;
    }
    
    
    /**
     * handles the message sending for each of the clients
     */
    public void run() {
        String[] splitinput;
        String pmName = null;
        String pmMessage = null;
        int pmFlag = 0;
        isClosed = false;
        
        try {
            this.out = new PrintWriter(client.getOutputStream(), false);
            this.inStream = client.getInputStream();
            Scanner in = new Scanner(client.getInputStream());
    
            String input = in.nextLine();
            
            /*checks if it is a welcome message and take its name*/
            if(input.indexOf('>') == -1){
                splitinput = input.split(" ", 2);
                setName(splitinput[1]);
            }
    
    
            while(!client.isClosed()) {
                if (in.hasNextLine()) {
                    input = in.nextLine();
    
                    if (input.contains(" /msg ")) {
                        //System.out.println("WE ARE IN THE PM THING");
                        splitinput = input.split(" ", 4);
                        pmName = splitinput[2];
                        pmMessage = splitinput[3];
                        pmFlag = 1;
        
                    }
                        for (ClientThread c : server.getClients()) {
                            if (pmFlag != 0) {
                                //System.out.println("Pm Flag Set " + "Sending message to: " + pmName );
                                //System.out.println("Message: " + pmMessage);
                                if (c.getName().equals(pmName)) {
                
                                    PrintWriter cout = c.getWriter();
                
                                    if (cout != null) {
                                        if (pmMessage != null) {
                                            cout.write("Private message from " + name + "> " + pmMessage + "\r\n");
                                            //System.out.println(input + "\r\n");
                                            cout.flush();
                                        }
                                    }
                                }
                            } else {
                                PrintWriter cout = c.getWriter();
            
                                if (cout != null) {
                                    cout.write(input + "\r\n");
                                    //System.out.println(input + "\r\n");
                                    cout.flush();
                                }
                            }
                        }
                        
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

   
}
