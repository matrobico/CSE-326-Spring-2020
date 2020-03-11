package GUI;
import com.mkyong.http.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class TextDemo extends JFrame implements ActionListener {

    JPanel panel;
    // Label to mark where to enter text
    JLabel text_label;
    JFrame textField;
    JButton submit, cancel;
    JTextArea textArea;

    TextDemo() {

        // Text Label
        text_label = new JLabel();
        text_label.setText("Enter text: ");

        // Submit
        submit = new JButton("SUBMIT");
        submit.addActionListener(this);

        // Text Area
        textArea = new JTextArea(10, 20);
        textField = new JFrame("Text Field");

        panel = new JPanel();
        panel.add(text_label);
        panel.add(submit);
        panel.add(textArea);

        textField.add(panel);
        textField.setSize(400, 300);
        textField.show();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the listeners to components.
        //add(panel, BorderLayout.CENTER);
        //setTitle("Messages");
        //setSize(300, 100);
        //setVisible(true);

    }

    public static void main(String[] args) {
        new TextDemo();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String s = ae.getActionCommand();
        if (s.equals("SUBMIT")) {
            System.out.println(textArea.getText());
            OkHttpExample obj = new OkHttpExample();
            try {
                obj.sendPost("51", "SuperSecretKey");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
