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

    /**
     * This is where everything starts. The user will be presented with the "Ephemeral.fxml" GUI
     * when they start the application. This function serves to load our beginning .fxml UI.
     *
     * The buttons on this screen are handled in "Controller.java". The GUIs for the respective
     * components are separate files.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Ephemeral.fxml"));
        primaryStage.setTitle("Ephemeral");
        primaryStage.setScene(new Scene(root, 665, 560));
        primaryStage.show();
        //primaryStage.setMaximized(true);

    }


    public static void main(String[] args) {
        launch(args);
    }

}
