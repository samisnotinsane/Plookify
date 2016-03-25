package common;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by Kristian on 03/03/16.
 */
public class openMainGUI extends Application{

    private static Stage stage;

    public void start(Stage primaryStage) throws Exception{
        this.stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("commonGUI.fxml"));
        primaryStage.setTitle("Plookify");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static Stage getStage(){
        return stage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
