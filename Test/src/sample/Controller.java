package sample;


import com.mkyong.http.*;
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
    private PasswordField createUser_password;
    @FXML
    private PasswordField createUser_repassword;
    @FXML
    private Label label2;
    @FXML
    private TextField msg;
    @FXML
    private TextArea display;

    OkHttpExample obj = new OkHttpExample();
    String authToken;


    /**
     * This function will take the user to the "messages" screen after clicking submit.
     *
     * The user will enter their username and password. Their input will be gathered here
     * and verified. If successful, they will continue. If not successful, they will be
     * notified and cannot proceed until a successful login.
     */
    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws Exception {
        //String authToken;
        String username = usr.getText();
        String password = pwd.getText();

        // Later change to: jpfPassword.getPassword()
        authToken = obj.login(username, password);
        if (authToken == null) {
            System.out.println("BEEP 0");
            label.setText("Incorrect password");
        } else {
            Parent chatViewParent = FXMLLoader.load(getClass().getResource("Chat.fxml"));
            Scene chatViewScene = new Scene(chatViewParent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setMaximized(true);
            window.setScene(chatViewScene);
            window.show();
        }

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
            Matcher matcher = pattern.matcher(createUser_password.getText());

            //boolean hasUppercase = !pswd.equals(pswd.getCharacters().toString().toLowerCase());
            int hasUppercase = createUser_password.getText().compareTo(createUser_password.getText().toLowerCase());
            //System.out.println(hasUppercase);

         if(createUser_password.getLength() < 8 && createUser_repassword.getLength() < 8){
             label2.setText("Length of password should be 8 or more");
            }
         else if(createUser_password.getText() != null && createUser_repassword.getText() != null &&
                 !createUser_password.getText().equals(createUser_repassword.getText())){
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

    /*
     @FXML protected void handleLogin(ActionEvent e){
            System.out.println("DO YOU SEE ME?");
     }

     */




     @FXML protected void handleSendChat(ActionEvent event){
            display.setText(msg.getText());
        }


     @FXML protected void onEnter(ActionEvent event){
            display.setText(msg.getText());
     }

    /**
     * Handles the actions the user will take to register an account.
     *
     * Upon clicking "Create User", the user will be taken to a registration
     * screen where they can enter a username and password. After they have
     * done so, they can then click on "Register", which will trigger this
     * function.
     */
    public void handleRegisterButtonAction(ActionEvent actionEvent) {

    }
}
