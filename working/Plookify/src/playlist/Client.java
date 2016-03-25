package playlist;/**
 * Created by sameenislam on 28/01/2016.
 * Responsible for handling JavaFX frontend client.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientUIMain.fxml"));
        primaryStage.setTitle("Plookify - v0.1 Pre Alpha Build");
        primaryStage.setScene(new Scene(root, 1100, 700));
        primaryStage.show();
    }
}
