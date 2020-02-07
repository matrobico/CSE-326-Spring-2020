package cse.oop.sgudmunson.hw9.two;
/**
 * Is the GUI frontend for a chat client server
 *
 *
 * @author Shadron Gudmunson
 * @version HW <9>, #<2>
 * @bugs Does not detect users leaving correctly
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ServerFrame extends JFrame {
    /**
     * creates a frame tied with server passed in
     * @param server the server to associate with the gui
     */
    public ServerFrame(Server server){
        ArrayList<String> clientNames = new ArrayList<>();
        
        JList<String> clientList = new JList<String>();
    
        add(clientList, BorderLayout.CENTER);
        
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //System.out.println("I happened!" + server.getClients().size());
                clientNames.clear();
                synchronized (server.getClients()) {
                    Iterator<ClientThread> iterator = server.getClients().iterator();
                    while (iterator.hasNext()) {
                        ClientThread c = iterator.next();
                        if (c.getClosed() == true) {
                            server.getClients().remove(c);
                        } else {
                            //System.out.println(c.getName() + " " + c.getClosed());
                            clientNames.add(c.getName());
                        }
                    }
                }
                String[] clientnames = new String[clientNames.size()];
    
                clientnames = clientNames.toArray(clientnames);
                
                clientList.setListData(clientnames);
            }
        };
    
        Timer updateClientList = new Timer(1000, al);
        updateClientList.start();
        
        pack();
    
        setTitle("Chat Server");
    }
    
    public static void main(String[] args){
        int port;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a port number: ");
        port = scan.nextInt();
        scan.close();
    
        Server server = new Server(port);
        
        ServerFrame serverframe = new ServerFrame(server);
        serverframe.setSize(250, 700);
        serverframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverframe.setVisible(true);
        
        server.startServerSocket();
    }
}
