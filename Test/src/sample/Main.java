package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import javax.swing.*;

public class Main extends Application{

        @Override
        public void start(Stage primaryStage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("Ephemeral.fxml"));
            primaryStage.setTitle("Ephemeral");
            primaryStage.setScene(new Scene(root, 300, 275));
            primaryStage.show();

        }


        public static void main(String[] args) {
            launch(args);
        }

}
