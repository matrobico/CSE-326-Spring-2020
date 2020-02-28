package Client;

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

import static Client.ClientFrame.chat;

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

            out_stream.println(
            "POST /articles HTTP/1.1\n" +
                    "Host: 192.168.1.10:3000\n" +
                    "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:73.0) Gecko/20100101 Firefox/73.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Referer: http://192.168.1.10:3000/articles/new\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: 188\n" +
                    "Origin: http://192.168.1.10:3000\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "Cookie: _blog_session=F6sOANM9cM5zewwfigcL5iTq2OxuGxCXQZFGA3%2F6ucOsvNYUQkMCYfXlwlaVctAtdsdXVrZrbw3SnlpK6P9aW9Vq%2FD6er6VhpFn%2FFGmWkICA2ndjO0VhNreeS4jwB52zwcup6ZAhceLHg4antn7P9BvFO3qNUg0DFtT3PUCor7wAfGoMEQtv6OmP5ybUEPePMQCOOMmMcU7%2FqxhNSw6nol7IqNqKtisJubZsvmIfeoTvCAvxC%2B6Xb%2FCC7H9SuKjiM7iXq9sytWpYA2pzXdXroj6JS0Ql--TT3y5wMumV6wGw%2Bz--%2BWgYfn1SIRx3laSdgBSZIQ%3D%3D\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "\n" +
                    "authenticity_token=Zkc7tNeWsxyz%2FnrSvH0H3n6Jz7vKNo7ifJJuSggzq%2BjDNfrdbrxMmixunq%2F140O1G%2FC8DvJjXlCkoWBm%2BaTN7g%3D%3D&article%5Btitle%5D=asdf&article%5Btext%5D=fsda&commit=Save+Article"
                    );
            //out_stream.println("POST / HTTP/1.0\n" + "Host: eph.nopesled.com\n" + name + "Logged in");
            out_stream.println();
            out_stream.flush();


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
                    out_stream.println();
                    out_stream.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
