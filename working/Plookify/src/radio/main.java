package radio;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class main extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    /**
     Load the GUI for the radio module
     */
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUIFindArtist.fxml"));
        loader.setController(new GUIFindArtistController());
        Parent root;

        root = loader.load();
        primaryStage.setTitle("Radio");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}