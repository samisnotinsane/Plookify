package playlist.view;

import account.Authenticator;
import account.Utilities;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import player.NowPlaying;
import playlist.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static playlist.view.PlaylistStore.getInstance;

/**
 * Created by sameenislam on 24/03/2016.
 */
public class MainControllerV2 implements Initializable {

    @FXML public ListView<Playlist> lstPlaylist;
    @FXML TableView<Track> tblTrack;
    @FXML public TableColumn plCol;
    @FXML public Button btnAddPlaylist;
    @FXML public Button btnTrack;
    @FXML public Button btnDelete;
    @FXML public TableColumn colSequenceNo;
    @FXML public TableColumn colTrack;
    @FXML public TableColumn colArtist;
    @FXML public TableColumn colDuration;
    @FXML public Label lblPlaylistName;
    @FXML public Label lblPrivacy;
    @FXML public Label lblPrivHeader;
    @FXML public Button btnSearch;
    @FXML public Button btnNowPlaying;
    @FXML public MenuButton mBtnMore;
    @FXML public TextField txtSearch;

    private ObservableList<Playlist> listViewPlaylists = FXCollections.observableArrayList(); // master playlist



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPlaylistNames();
        // populate tableView with playlist tracks
        populatePlaylistTableView();
        listViewChangeHandler();

    }

    /***************************************************
     * Gets saved playlists and populates the listview *
     ***************************************************/
    private void loadPlaylistNames() {
//        System.out.println("calling loadPlaylistNames");

        try {
            listViewPlaylists = PlaylistManager.loadAllPlaylists();
        } catch(Exception e) {
//            System.out.println("EXCEPTION in loadPlaylistNames!");
        }

//        System.out.println("Populating listView");
        lstPlaylist.setEditable(true);
        lstPlaylist.setItems(listViewPlaylists);


//        System.out.println("returning from loadPlaylistNames");
    }

    /***********************************
     * Button handler for Add Playlist *
     ***********************************/
    public void addNewPlaylist(ActionEvent event) {
//        System.out.println("Calling showNewPlaylistDialog");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Playlist");
        dialog.setHeaderText("Create new playlist");
        dialog.setContentText("Name:");

        // grab input
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            String input = result.get();
            if(validateForLetters(input)) {
                // input is valid; continue creating playlist...

//                System.out.println("Creating new playlist...");
                try {
                    int userid = Utilities.getUserID(Authenticator.getUser().getEmail());
                    // set privacy to private by default
                    String priv = "private";

                    //Playlist constr requires: String name, int owner, String privacy
                    Playlist playlist = new Playlist(input, userid, priv); // NOTE: this will be committed to DB!

                    // finally add this Playlist object to ListView to display results
                    listViewPlaylists.add(playlist);
                } catch (Exception e) {
//                    System.out.print("FAIL!\n");
                }
//                System.out.print("OK!\n");

            } else {
                // alert: invalid input
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Plookify");
                alert.setHeaderText("Caution");
                alert.setContentText("Playlist names can only consist of letters.");
                alert.showAndWait();

            }
        }
    }

    /**************************************
     * Button handler for search playlist *
     **************************************/
    public void searchPlaylistName(ActionEvent event) {

    }


    /**************************************
     * Button handler for btnRename *
     **************************************/
    public void btnRenameEventHandler(ActionEvent event) {
        String name = showRenameDialog();
        Playlist pl = PlaylistStore.getInstance().getPlaylist();

        SQL.renamePlaylist(name, pl.getId());
        try {
            loadPlaylistNames();
        } catch (Exception e) {

        }
    }

    /**************************************
     * Rename dialog *
     **************************************/
    public String showRenameDialog() {
//        System.out.println("Calling showRenameDialog");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename Playlist");
        dialog.setHeaderText("Rename");
        dialog.setContentText("New name:");

        // grab input
        Optional<String> result = dialog.showAndWait();
        String input = "";
        if(result.isPresent()) {
           input  = result.get();
        }
//        System.out.println("returning from showRenameDialog");
        return input;
    }


    /**********************************
     * Button handler for Track Button *
     **********************************/
    public void showTrackWindow(ActionEvent event) {
//        System.out.println("Opening track list");
        try {
            Launcher.showTrackWindow(event);
        } catch (Exception e) {
//            System.out.println("An exception occurred while attempting to open track list!");
        }
    }


    /**********************************
     * Button handler for Delete Button *
     **********************************/
    public void btnDeleteHandler(ActionEvent event) {

        try {
            Playlist playlist = PlaylistStore.getInstance().getPlaylist();
            SQL.removePlaylist(playlist.getId());
            loadPlaylistNames();
        } catch (Exception e) {
//            System.out.println("An exception occurred while attempting to delete!");
        }
    }

    /**********************************
     * Button handler for now playing Button *
     **********************************/
    public void btnNowPlayingHandler(ActionEvent event) {
        try {
            Playlist pl = PlaylistStore.getInstance().getPlaylist();
            pl.loadContents();
            NowPlaying.getInstance().addPlaylist2(SQL.trackIdList);
        } catch (Exception e) {

        }
    }


    /***********************************
     * Change handler for list view    *
     ***********************************/
    public void listViewChangeHandler() {
        MultipleSelectionModel<Playlist> lvSelModel = lstPlaylist.getSelectionModel();
        lvSelModel.selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            @Override
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {

                //System.out.println("Setting header to: "+newValue.getName()+" & privacy label to: "+ newValue.getType());
                try {
                    lblPlaylistName.setText(newValue.getName());
                    lblPrivacy.setText(newValue.getType());
                    lblPrivHeader.setText("");
                } catch (Exception e) {

                }

                populatePlaylistTableView();
//                System.out.println("Setting playlist");
                // cache the object in a singleton for use in the TrackController
                PlaylistStore.getInstance().setPlaylist(newValue);

            }
        });

    }

    /************************************************************
     * Populates tableview with tracks from a selected playlist *
     ************************************************************/
    private void populatePlaylistTableView() {
        Label placeholderLabel = new Label("There are no songs in this playlist :/");
        tblTrack.setPlaceholder(placeholderLabel);
        tblTrack.setEditable(true);

        try {
            colSequenceNo.setCellValueFactory(new PropertyValueFactory<Track, Integer>("sequenceNo"));
            colTrack.setCellValueFactory(new PropertyValueFactory<Track, String>("name"));
            colArtist.setCellValueFactory(new PropertyValueFactory<Track, String>("artist"));
            colDuration.setCellValueFactory(new PropertyValueFactory<Track, String>("duration"));

            ObservableList<Track> playlistTracks = PlaylistStore.getInstance().getPlaylist().getTrackList();
//            System.out.println(">>> "+playlistTracks.toString()+"\n");
            tblTrack.setItems(playlistTracks);

        } catch(Exception e) {
//            System.out.println("EXCEPTION in populateTableView!");
        }
    }

    // returns false if string contains only letters
    private boolean validateForLetters(String str) {
        boolean valid = false;
        if (str.matches("[a-zA-Z0-9_\\s]+")) { // only letters & numbers and single whitespaces
            valid = true;
//            System.out.println("valid input");
        } else
//            System.out.println("invalid input");
        return valid;
        return str.chars().allMatch(x -> Character.isLetter(x));
    }
}
