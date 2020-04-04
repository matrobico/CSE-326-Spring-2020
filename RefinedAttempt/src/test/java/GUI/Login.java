package GUI;
import com.mkyong.http.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.View;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Basic idea taken from https://www.onlinetutorialspoint.com/java/java-swing-login-example.html
// But extensively modified to fit our needs
class LoginDemo extends JFrame implements ActionListener {

    JPanel panel;
    JLabel user_label, password_label, message;
    JTextField username_text;
    JPasswordField password_text;
    JButton submit, cancel;

    LoginDemo() {

        // User Label
        user_label = new JLabel();
        user_label.setText("User Name :");
        username_text = new JTextField();

        // Password
        password_label = new JLabel();
        password_label.setText("Password :");
        password_text = new JPasswordField();

        // Submit
        submit = new JButton("SUBMIT");
        // Cancel
        //cancel = new JButton("CANCEL");

        panel = new JPanel(new GridLayout(3, 1));

        panel.add(user_label);
        panel.add(username_text);
        panel.add(password_label);
        panel.add(password_text);

        message = new JLabel();
        panel.add(message);
        panel.add(submit);
        //panel.add(cancel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Please Login Here !");
        setSize(300, 100);
        setVisible(true);

    }

    public String getUsername() {
        return username_text.getText().trim();
    }

    public String getPassword() {
        return password_text.getSelectedText().trim();
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
        String username = username_text.getText();
        String password = password_text.getText();

        // Declaring a authorization token. It should never stay this value if a proper authToken returns
        String authToken = "NOPE";
        OkHttpExample obj = new OkHttpExample();
        try {
            authToken = obj.login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (authToken.equals("NOPE")) {
            System.out.println("Invalid login...");
        }

        System.out.println("LOGIN SUCCESSFUL");
        try {
            //new ViewMessageList();
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}