package common;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by tahnik on 15/03/2016.
 */
public class MainGUI extends Stage{
    public MainGUI() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("commonGUI.fxml"));
        this.setTitle("Plookify");
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.show();
        this.setResizable(false);
        this.centerOnScreen();
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
