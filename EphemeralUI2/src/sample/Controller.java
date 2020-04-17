package sample;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

import javax.swing.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
        @FXML
        private Label label;
        @FXML
        private Text actiontarget;
        @FXML
        private TextField usr;
        @FXML
        private TextField pwd;
        @FXML
        private TextField user;
        @FXML
        private PasswordField pswd;
        @FXML
        private PasswordField repswd;
        @FXML
        private Label label2;
        @FXML
        private TextField msg;
        @FXML
        private TextArea display;

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

    @FXML protected void handleCreateUserButtonAction(ActionEvent event) throws IOException {
        Parent chatViewParent = FXMLLoader.load(getClass().getResource("CreateUser.fxml"));
        Scene chatViewScene = new Scene(chatViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(chatViewScene);
        window.show();
    }

    @FXML protected void handleReturnButtonAction(ActionEvent event) throws IOException {
        Parent chatViewParent = FXMLLoader.load(getClass().getResource("Ephemeral.fxml"));
        Scene chatViewScene = new Scene(chatViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(chatViewScene);
        window.show();
    }

     @FXML public void handleKeyReleased(KeyEvent e){
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
            Matcher matcher = pattern.matcher(pswd.getText());

            //boolean hasUppercase = !pswd.equals(pswd.getCharacters().toString().toLowerCase());
            int hasUppercase = pswd.getText().compareTo(pswd.getText().toLowerCase());
            //System.out.println(hasUppercase);

         if(pswd.getLength() < 8 && repswd.getLength() < 8){
             label2.setText("Length of password should be 8 or more");
            }
         else if(pswd.getText() != null && repswd.getText() != null && !pswd.getText().equals(repswd.getText())){
             label2.setText("The passwords do not match! Please try again.");
            }
         else if(matcher.matches()){
             label2.setText("Password must contain a symbol (@, $, etc...)");
            }
         else if(hasUppercase == 0){
             label2.setText("Password must contain at least one upper case letter");
            }
         else
             label2.setText("Username and password are valid");
        }


     /*@FXML protected void handleLogin(ActionEvent e){
         String authToken = "";
         OkHttpExample obj = new OkHttpExample();

         System.out.println("Register User");
         obj.registerUser("user3", "asdfasdf", "asdfasdf");

         System.out.println("Logging in");
         authToken = obj.login("user3", "asdfasdf");

         System.out.println(authToken);

         //System.out.println("Testing 1 - Send Http POST request");
         //obj.sendPost("Based on an agreement that was reached in 1966 between Iran and Romania to establish a tractor manufacturing company in Iran, the company was created in Tabriz in 1968. The first goal of the company was to manufacture 10,000 units tractors of 45-65 horsepower engines with single and double differential gearboxes. In 1976 Massey Ferguson started to assemble tractors in the company with the rate of 13000 units for each year. At the moment the production capacity has been increased up to 30000 units for each year.[1] On 1987 the factory started to increase its foundry capacity to be able to produce casting products for different industries. Nowadays it has the largest foundry capacity among middle east. In 1990s The factory started to produce small trucks and vans behind its main products. ", "SuperSecretKey", authToken);

         System.out.println("Testing 2 - Send Http GET request");
         obj.sendGet("SuperSecretKey", authToken);
     }*/




     @FXML protected void handleSendChat(ActionEvent event){
            display.setText(msg.getText());
        }


     @FXML protected void onEnter(ActionEvent event){
            display.setText(msg.getText());
     }
}
