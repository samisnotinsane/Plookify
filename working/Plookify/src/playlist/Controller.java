package playlist;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by sameenislam on 28/01/2016.
 * FXML Controller. Handles client UI events.
 */
public class Controller {

    public void newPlaylistBtnClicked() throws Exception {
        System.out.println("New Playlist btn clicked.");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewPlaylistNameDialog.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Create New Playlist");
        stage.setScene(new Scene(root, 450, 100));
        stage.show();
    }

    public void removePlaylistBtnClicked() {
        System.out.println("Remove Playlist btn clicked.");
    }

    public void renamePlaylistBtnClicked() {
        System.out.println("Rename Playlist btn clicked.");
    }

    public void keyBeingPressedInSearchbox() {
        System.out.println("key being pressed in searchbox.");
    }

    public void searchBtnClicked() {
        System.out.println("Search btn was clicked.");
    }

}
