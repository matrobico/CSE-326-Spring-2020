package GUI;
import com.mkyong.http.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Main class for the demo. Run this to start the demo.
public class Demo extends JFrame {
    private LoginDialog loginDialog;

    public Demo() {
        loginDialog = new LoginDialog(this, true);
        loginDialog.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        /*
        LoginDemo LD = new LoginDemo();
        boolean check_auth = LD.getValue();
        System.out.println(check_auth);

        if (check_auth) {
            System.out.println("DO YOU SEE ME?");
            new ViewMessageList().main(null);
        }

         */

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                new Demo();
                //JFrame frame = new Demo();
                //frame.getContentPane().setBackground(Color.BLACK);
                //frame.setTitle("Logged In");
                //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //frame.setLocationRelativeTo(null);
                //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


                try {
                    new ViewMessageList().main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
