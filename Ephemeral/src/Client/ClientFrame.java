package Client;
/**
 * Is the GUI frontend for a chat client
 *
 *
 * @author Shadron Gudmunson
 * @version HW <9>, #<1>
 * @bugs none
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class ClientFrame extends JFrame {
    private Client client;
    public static JTextArea chat;
    
    /**
     * sets the client attribute
     * @param client value to set client to
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    /**
     * gets the client
     * @return client
     */
    public Client getClient() {
        return client;
    }
    
    /**
     * creates a gui for a Chat client
     * @param port port of the server to connect to
     * @param name user name of the client
     */
    public ClientFrame(int port, String name){
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    
        this.client = new Client(port, name);
    
        JPanel bottom = new JPanel();
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Submit");
        
        this.chat = new JTextArea();
        chat.setEditable(false);
    
        bottom.add(messageField);
        pack();
    
        bottom.add(sendButton);
        pack();
        
        add(bottom, BorderLayout.SOUTH);
        
        messageField.setColumns(50);
        
        add(chat, BorderLayout.CENTER);
        pack();
        
        client.getServer_thread().newMessage("Welcome " + name);
        /*allows me to press enter instead of send button every time*/
        KeyListener sendmssg = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    String message = messageField.getText();
                    if (!message.equals("")) {
                        messageField.setText("");
                        client.getServer_thread().newMessage(name + "> " + message);
                    }
                }
            }
        };
        
        messageField.addKeyListener(sendmssg);
    
        ActionListener sendMessage = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String message = messageField.getText();
                if (!message.equals("")){
                    messageField.setText("");
                    client.getServer_thread().newMessage(name + "> " + message);
                }
            }
        };
        
        sendButton.addActionListener(sendMessage);
            
        setSize(700,700);
        setTitle("Chat Client");
        }
        
        
        public static void main(String[] args){
            int port;

            Scanner scan = new Scanner(System.in);
            port = 443;
    
            String name = null;
            System.out.print("Enter a name: ");
            name = scan.nextLine();
            
            ClientFrame frame = new ClientFrame(port, name);
    
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
}
