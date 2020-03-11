/*
Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.

I'm not sure if the above copyright is even needed, because this code is not
and exact replica of the TextDemo.java tutorial. It simply spawned from it.
However, I'll include it anyway.

We'll role our own JavaFX implementation when we figure out the current errors
with it. This is a makeshift GUI for now. It exists for demo purposes.
 */


package GUI;

import com.mkyong.http.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewMessageList extends JPanel implements ActionListener {
    protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";

    public ViewMessageList() {
        super(new GridBagLayout());

        textField = new JTextField(20);
        textField.addActionListener(this);

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);

        refreshMessages();
    }

    /*
     The problem (one of them) with this at the moment, is that every time you
     press to refresh, it displays ALL the messages from the web server. Ideally,
     We want it to display only new messages every time. I'll figure this out later.

     Also, performing any action both refreshes the messages like above AND sends a
     POST request for the entered text. Obviously we don't want that (at least not like
     it is now), so that also needs to be fixed. 
     */
    public void refreshMessages() {
        OkHttpExample obj = new OkHttpExample();
        try {
            for (String s : obj.sendGet("51", "SuperSecretKey")) {
                textArea.append(s + newline);
                textField.selectAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent evt) {
        // Testing things out
        refreshMessages();

        /*
        String text = textField.getText();
        textArea.append(text + newline);
        textField.selectAll();
         */

        OkHttpExample obj = new OkHttpExample();
        try {
            obj.sendPost("51", "SuperSecretKey");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new ViewMessageList());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
}
