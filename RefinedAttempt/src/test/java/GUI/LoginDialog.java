package GUI;

/*
Structure from: https://stackoverflow.com/questions/22959558/how-can-i-make-code-wait-for-a-gui-to-finish
Modified to fit our needs, more extensive modification underway
 */

import com.mkyong.http.OkHttpExample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class LoginDialog extends JDialog {
    private final JLabel jlblUsername = new JLabel("Username");
    private final JLabel jlblPassword = new JLabel("Password");

    private final JTextField jtfUsername = new JTextField(15);
    private final JPasswordField jpfPassword = new JPasswordField();

    private final JButton jbtOk = new JButton("Login");
    private final JButton jbtCancel = new JButton("Cancel");
    private final JButton jbtRegister = new JButton("Register");

    private final JLabel jlblStatus = new JLabel(" ");

    String authToken;
    OkHttpExample obj = new OkHttpExample();

    public LoginDialog() {
        this(null, true);
    }

    public String getAuthToken() {
        return authToken;
    }

    public OkHttpExample getObj() {
        return obj;
    }

    // Login GUI setup
    public LoginDialog(final JFrame parent, boolean modal) {
        super(parent, modal);

        JPanel p3 = new JPanel(new GridLayout(2, 1));
        p3.add(jlblUsername);
        p3.add(jlblPassword);

        JPanel p4 = new JPanel(new GridLayout(2, 1));
        p4.add(jtfUsername);
        p4.add(jpfPassword);

        JPanel p1 = new JPanel();
        p1.add(p3);
        p1.add(p4);

        JPanel p2 = new JPanel();
        p2.add(jbtOk);
        p2.add(jbtCancel);
        p2.add(jbtRegister);

        JPanel p5 = new JPanel(new BorderLayout());
        p5.add(p2, BorderLayout.CENTER);
        p5.add(jlblStatus, BorderLayout.NORTH);
        jlblStatus.setForeground(Color.RED);
        jlblStatus.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new BorderLayout());
        add(p1, BorderLayout.CENTER);
        add(p5, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });


        // ActionListener for when user clicks "login"
        jbtOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //boolean flag = true;

                try {
                    // Later change to: jpfPassword.getPassword()
                    authToken = obj.login(jtfUsername.getText(), jpfPassword.getText());
                    if (authToken == null) {
                        jlblStatus.setText("Invalid username or password");
                        //System.out.println("PROGRESS");
                    } else {
                        parent.setVisible(true);
                        setVisible(false);
                    }

                } catch (Exception ex) {
                    jlblStatus.setText("Invalid username or password");
                    //ex.printStackTrace();
                }


            }
        });

        // ActionListener for when user clicks "cancel"
        jbtCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                parent.dispose();
                System.exit(0);
            }
        });

        // ActionListener for when user clicks "register"
        jbtRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    obj.registerUser(jtfUsername.getText(), jpfPassword.getText(), jpfPassword.getText());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
