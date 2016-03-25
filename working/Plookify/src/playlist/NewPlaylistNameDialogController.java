package playlist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


/**
 * Created by sameenislam on 29/01/2016.
 * Controller for 'New Playlist' dialog box.
 */
public class NewPlaylistNameDialogController {

    @FXML private TextField playlistName;

    @FXML
    private void getPlaylistName(ActionEvent event) {
        System.out.println("NewPlaylistNameDialog received input: '"+playlistName.getText() + "' via return key.");
    }

    /*@TODO
     * Close modal dialog when 'Cancel' is clicked.
     */
    public void cancelBtnClicked() {
        System.out.println("New playlist creation cancelled.");

    }

    public void okBtnClicked() {
        System.out.println("New playlist creation confirmed");
        System.out.println("NewPlaylistNameDialog received input: '"+playlistName.getText() + "' via OK btn.");
        //playlistName.getText();
    }
}
