package playlist;

import common.ComponentLoader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by sameenislam on 11/03/2016.
 */
public class Launcher extends Application {

    private static Stage window;
    private static Scene loginScene, homeScene, trackScene;


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Playlists - Plookify v0.5 Alpha");
        window.initStyle(StageStyle.DECORATED);
        showLogin();
    }

    public static void showLogin() throws IOException {
        Parent root = FXMLLoader.load(Launcher.class.getResource("view/login.fxml"));
        loginScene = new Scene(root);
        window.centerOnScreen();
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(loginScene);
        window.show();

    }

    public static void showHome(ActionEvent event) throws IOException {
        // Get root node and place it in scene
        FXMLLoader load = ComponentLoader.getInstance().loadFXML("/playlist/view/Main.fxml");
    }

    public static void showTrackWindow(ActionEvent event) throws IOException {
        FXMLLoader load = ComponentLoader.getInstance().loadFXML("/playlist/view/Track.fxml");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
