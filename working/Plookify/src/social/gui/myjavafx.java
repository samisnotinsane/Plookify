package social.gui;

import javafx.application.Application;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Authenticator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class myjavafx extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {

            AnchorPane page = (AnchorPane) FXMLLoader.load(myjavafx.class.getResource("Social3.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Social");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(myjavafx.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}