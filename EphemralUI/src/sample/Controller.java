package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class Controller {
        final Label label = new Label();
        @FXML
        private Text actiontarget;
        @FXML
        private TextField usr;
        @FXML
        private TextField pwd;
        @FXML
        private TextField msg;
        @FXML
        private TextField display;

        /*@FXML public void handleObtainInfo(ActionEvent e){
            if(pwd.getText() != null && usr.getText() != null){
                label.setText(usr.getText() + " " + pwd.getText());
            }
        }*/


        @FXML protected void handleSubmitButtonAction(ActionEvent event) throws IOException {


            Parent chatViewParent = FXMLLoader.load(getClass().getResource("Chat.fxml"));
            Scene chatViewScene = new Scene(chatViewParent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(chatViewScene);
            window.show();
        }

        /*@FXML protected void handleSendChat(ActionEvent event){
            String text = msg.getText();
            display.appendText(text + "\n");
            display.selectAll();
            return;
        }*/

}
