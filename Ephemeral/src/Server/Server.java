package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
/**
 * back end server for chat client
 *
 *
 * @author Shadron Gudmunson
 * @version HW <9>, #<2>
 * @bugs Does not detect users leaving correctly
 */

/**
 * Created by ritakuo on 4/9/19.
 */
public class Server {
    private int port;
    private final ArrayList<ClientThread> clients;
    
    /**
     * gets the list of clients
     * @return the list of clients
     */
    public ArrayList<ClientThread> getClients() {
        return clients;
    }
    
    /**
     * constructor for the server object
     * @param port the port to start the server listening on
     */
    public Server(int port) {
        this.port = port;
        clients = new ArrayList<ClientThread>();
    }
    
    /**
     * starts the server listening on the ports specified in constructor.
     */
    public void startServerSocket(){
        // Begin the server socket
        ServerSocket server_socket = null;
        try {
            server_socket = new ServerSocket(port);
            System.out.println("Server Started: Ready for client connections");
            // Waiting for client's connection request
            acceptClient(server_socket);
        } catch (IOException e) {
            System.out.println("Failed to start the server on port: " + port);
            e.printStackTrace();
        }
        
    }

    private void acceptClient(ServerSocket server_socket) {
        synchronized (clients) {
            Iterator<ClientThread> iterator = getClients().iterator();
            while (iterator.hasNext()) {
                ClientThread s = iterator.next();
                if (s.getName() != null) {
                    try {
                        if (s.getInStream().read() == -1) {
                            s.setClosed(true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        while(true) {
            try {
                // Accept new connection from the client
                Socket client = server_socket.accept();
                System.out.println("Accept new connection from: " + client.getRemoteSocketAddress());

                // Each client is running in the individual thread
                ClientThread client_thread = new ClientThread(this, client);
                Thread new_client = new Thread(client_thread);
                new_client.start();
                
                clients.add(client_thread);
    
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    public static void main(String[] args) {
        int port;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a port number: ");
        port = scan.nextInt();
        scan.close();

        Server server = new Server(port);

    }
}
