package GUI;
import com.mkyong.http.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Simply a public class used for authenticating a login
public class Demo {
    public static void main(String[] args) throws Exception {
        new LoginDemo();

        new ViewMessageList().main(null);
    }
}
