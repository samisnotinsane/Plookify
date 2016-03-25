package playlist.view;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import playlist.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by sameenislam on 14/03/2016. ****DEPRECATED class****
 */
public class MainController implements Initializable {

    @FXML public ListView<Playlist> lstPlaylist;
    @FXML TableView<Track> tblTrack = new TableView<>();
    @FXML public TableColumn plCol;
    @FXML public Button btnAddPlaylist;
    @FXML public TableColumn colSequenceNo;
    @FXML public TableColumn colTrack;
    @FXML public TableColumn colArtist;
    @FXML public TableColumn colDuration;
    @FXML public Label lblPlaylistName;
    @FXML public Label lblPrivacy;
    @FXML public Label lblPrivHeader;
    @FXML public Button btnSearch;
    @FXML public MenuButton mBtnMore;
    @FXML public TextField txtSearch;

    private Playlist selectedPlaylist;

    // stores a list of playlist names. Comes from the database.
    private ObservableList<Playlist> list = FXCollections.observableArrayList(); // master playlist
    private ObservableList<Playlist> filteredPlaylist = FXCollections.observableArrayList(); // filtered version of master playlist
    private ObservableList<Track> trackList = FXCollections.observableArrayList();

    // Track stage used to view a list of all tracks
    private static Stage trackWindow;
    private static Scene trackScene;

    // class constructor - called before initialize()
    public MainController() {

        try {
        filteredPlaylist.addAll(list);
            // listening for changes to the master list
            list.addListener(new ListChangeListener<Playlist>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Playlist> change) {
                    updateFilteredData();
                }
            });
        } catch(Exception e) {
            //System.out.println("EXCEPTION in MainController constructor!");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        initialisePlaylistNameFilter();
        fetchPlaylists();
        populateListView();
        populateTableView();

        lblPrivacy.setText("");
        lblPrivHeader.setText("Ready to select a playlist to view contents");

        // test
//        ObservableList<Integer> rawTracks = SQL.selectTracksMatchingPlaylist(10);
//        System.out.print("Testing PLAYLISTTRACK table...");
//        if(!(rawTracks == null)) {
//            System.out.print("OK!\n");
//        }
//        System.out.print("\nReady.\n\n");

    }

    /***************************
     * Called from initialize()*
     ***************************/
    // get all names of stored playlists from database and stores them in the
    // ObservableArrayList
    public void fetchPlaylists(){
        //System.out.println("calling fetchPlaylists");
        //ObservableList playlistNameData = PlaylistManager.showStored(); // returns an ObservableList<String>
        ObservableList<Playlist> playlistNameData = FXCollections.observableArrayList();
        try {
            playlistNameData = PlaylistManager.loadAll();

            for(int i=0; i<playlistNameData.size(); i++) {
                list.add(playlistNameData.get(i));
            }
        } catch(Exception e) {
            //System.out.println("EXCEPTION in fetchPlaylists!");
        }
        //System.out.println("returning from fetchPlaylists");
    }

    /***************************
     * Called from initialize()*
     ***************************/
    // initialises the listView
    public void populateListView(){
        //System.out.println("Populating table...");
        lstPlaylist.setEditable(true);
        lstPlaylist.setItems(filteredPlaylist);

        selectionMonitor(); // monitors what's being selected in list
        contextMenuMonitor();
    }


    /***************************
     * Called from initialize()*
     ***************************/
    // populates columns with the correct data
    private void populateTableView() {
        //System.out.println("Calling populateTableView");
        // shown by default upon loading and when playlist is empty
        Label placeholderLabel = new Label("There are no songs in this playlist :/");
        //Button placeholderButton = new Button("+ Add Track"); // open track.fxml
        VBox placeholderLayout = new VBox(10);
        placeholderLayout.setPadding(new Insets(200,100,100,160));
        placeholderLayout.getChildren().add(placeholderLabel);
        tblTrack.setPlaceholder(placeholderLayout);
        tblTrack.setEditable(true);

        // populate columns
        try {
            colSequenceNo.setCellValueFactory(new PropertyValueFactory<Track, Integer>("sequenceNo"));
            colTrack.setCellValueFactory(new PropertyValueFactory<Track, String>("name"));
            colArtist.setCellValueFactory(new PropertyValueFactory<Track, String>("artist"));
            colDuration.setCellValueFactory(new PropertyValueFactory<Track, String>("duration"));
            tblTrack.setItems(trackList);
        } catch(Exception e) {
            //System.out.println("EXCEPTION in populateTableView!");
        }
        //System.out.println("returning from populateTableView");
    }

    // opens a playlist and shows its Tracks
    private void showPlaylistContentsInTable(Playlist playlist) {
        //System.out.println("Calling showPlaylistContentsInTable");

        // use playlist id to find the matching
        int playlistId = playlist.getId();
//        trackList = playlist.getTrackList(); // make it return a list of track objects

        //System.out.println("Printing contents of trackList...");
        for (Track trk : trackList) {
            //System.out.println(trk.toString());
        }

        populateTableView();
        //System.out.println("returning from populateTableView");
    }



    private void selectionMonitor() {


        //System.out.println("Calling selectionMonitor");
        MultipleSelectionModel<Playlist> lvSelModel = lstPlaylist.getSelectionModel();
        // changelistener for monitoring change of selection with the listview

        lvSelModel.selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            @Override
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {

                // oldValue is the previous selection,
                // newValue is the new selection (text)
                try {
                    selectedPlaylist = newValue;
                }catch(Exception e){
                    //System.out.println("EXCEPTION in SelectionMonitor, selectedPlaylist=newValue!");
                }
                //System.out.println("playlist loaded: ["+newValue+", id="+selectedPlaylist.getId()+"]");
                //System.out.println("Setting header to: "+newValue.getName()+" & privacy label to: "+ newValue.getType());
                lblPlaylistName.setText(newValue.getName());
                lblPrivacy.setText(newValue.getType());
                lblPrivHeader.setText("Privacy:");
                // use newValue to load the correct Playlist object and display it's Tracks in the TableView...
                try {
                    showPlaylistContentsInTable(newValue);
                } catch (Exception e) {
                    //System.out.println("EXCEPTION in SelectionMonitor, showPlaylistContentsInTable!");

                }
                try {
                    storeSelectedPlaylist(newValue);
                } catch (Exception e) {
                    //System.out.println("EXCEPTION in SelectionMonitor, storeSelectedPlaylist!");
                }
                try {
                    updateFilteredData();
                } catch (Exception e) {
                    //System.out.println("EXCEPTION in SelectionMonitor, updateFilteredData!");
                }

            }
        });
        //System.out.println("Returning from selectionMonitor");
    }

    /*
     * stores a reference in the PlaylistManager singleton so that
     * tracks can be added to the referenced object from Track.fxml
     */
    private void storeSelectedPlaylist(Playlist playlist) {
        PlaylistStore.getInstance().setPlaylist(playlist); // called when a playlist selection is made
    }


    /* responsible for invoking the context menu
     * and handling events generated by clicking on the menu items
     */
    private void contextMenuMonitor() {
        //System.out.println("Calling contextMenuMonitor");
        // invoke context menu upon right-clicking on the listview
        lstPlaylist.setOnMousePressed(new EventHandler<MouseEvent>() {
            ContextMenu playlistContextMenu = new ContextMenu();
            MenuItem deleteMenuItem = new MenuItem();
            MenuItem addTrackMenuItem = new MenuItem();
            MenuItem renameMenuItem = new MenuItem();


            @Override
            public void handle(MouseEvent t) {


                if (t.isSecondaryButtonDown()) {
                    // invoke contextmenu now
                    //System.out.println("invoking context menu");
                    deleteMenuItem = new MenuItem("Delete");
                    addTrackMenuItem = new MenuItem("Add tracks to this playlist");
                    renameMenuItem = new MenuItem("Rename");
                    // event handlers for the contextmenu...

                    // delete
                    deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            //System.out.println("Playlist deleted");
                            deletePlaylist(selectedPlaylist);

                        }
                    });

                    // rename selected playlist
                    renameMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //System.out.println("Renaming playlist");
                            showRenameDialog();
                            // clear and fetch again
                            list.clear();
                            fetchPlaylists();

                        }
                    });

                    addTrackMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            // call add track scene...
                            //System.out.println("opening track.fxml");
                            try {
                                Launcher.showTrackWindow(e);
                            } catch (IOException er) {
                                //System.out.println("Error loading track window!");
                            }
                        }
                    });

                    playlistContextMenu.getItems().addAll(deleteMenuItem, renameMenuItem, addTrackMenuItem);

                    // show context menu
                    playlistContextMenu.show(lstPlaylist, t.getScreenX(), t.getScreenY());

                }
                else if (t.isPrimaryButtonDown()) {
                    playlistContextMenu.getItems().removeAll(deleteMenuItem, renameMenuItem, addTrackMenuItem);
                    playlistContextMenu.hide();
                }
            }
        });
        //System.out.println("returning from contextMenuMonitor");
    }

    // shows rename dialog
    public void showRenameDialog() {
        //System.out.println("Calling showRenameDialog");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename Playlist");
        dialog.setHeaderText("Rename");
        dialog.setContentText("New name:");

        // grab input
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            String input = result.get();
            if(validateForLetters(input)) {
                // input is valid; continue renaming
                PlaylistStore.getInstance().getPlaylist().rename(input);

                //clearListView(); // TODO: refresh list upon adding a new playlist
            } else {
                // alert: invalid input
                displayAlertMessage("Caution", "Playlist names can only consist of letters.");
            }
        }
        //System.out.println("returning from showRenameDialog");
    }

    // delete playlist from the database, and the ListView
    private void deletePlaylist(Playlist selectedPlaylist) {
        //System.out.println("Calling deletePlaylist");
        // first remove from list
        list.remove(selectedPlaylist); // >> test <<

        // call function to remove from db
        Playlist.delete(selectedPlaylist);
        //System.out.println("returning from deletePlaylist");
    }


    // creates a new playlist object and stores it in the dab
    public void addNewPlaylist() {
        //System.out.println("Calling addNewPlaylist");
        // Show a dialog which takes in a playlist
        showNewPlaylistDialog();
        //System.out.println("returning from addNewPlaylist");
    }

    private void showNewPlaylistDialog() {
        //System.out.println("Calling showNewPlaylistDialog");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Playlist");
        dialog.setHeaderText("Create new playlist");
        dialog.setContentText("Name:");

        // grab input
        Optional<String> result = dialog.showAndWait();
            if(result.isPresent()) {
                String input = result.get();
                if(validateForLetters(input)) {
                    // input is valid; continue...
                    createPlaylist(input);
                    //clearListView();
                } else {
                    // alert: invalid input
                    displayAlertMessage("Caution", "Playlist names can only consist of letters.");
                }
            }
        //System.out.println("returning from showNewPlaylistDialog");
    }

    // used for refreshing the playlist name view
    private void clearListView() {
//        lstPlaylist.setItems(null);
//        list.removeAll();
//        fetchPlaylists();

    }

    private void displayAlertMessage(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Plookify");
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // returns false if string contains only letters
    private boolean validateForLetters(String str) {
        boolean valid = false;
        if (str.matches("[a-zA-Z0-9_\\s]+")) { // only letters & numbers and single whitespaces
            valid = true;
            //System.out.println("valid input");
        } else
            //System.out.println("invalid input");
        return valid;
        return str.chars().allMatch(x -> Character.isLetter(x));
    }

    /*
     * Creates a new playlist entry into the database and
     * adds it to the list in the UI.
     */

    private void createPlaylist(String name) {
        //System.out.println("Calling createPlaylist");
        // get owner
        try {
            int userid = User.getId();

            // set privacy to private by default
            String priv = "private";

            //Playlist constr requires: String name, int owner, String privacy
            Playlist playlist = new Playlist(name, userid, priv); // NOTE: this l will be committed to DB!

            // finally add this Playlist object to ListView to display results
            list.add(playlist);
        } catch (Exception e) {
            //System.out.println("EXCEPTION in createPlaylist!");
        }
        //System.out.println("returning from createPlaylist");
    }

    // invoked by btnSearch
    @FXML
    private void searchPlaylistName(ActionEvent event) {
        //System.out.println("Calling searchPlaylistName");
        try {
            updateFilteredData();
        } catch(Exception e) {
            //System.out.println("EXCEPTION in searchPlaylistName!");
        }
        //System.out.println("returning from searchPlaylistName");
    }

    // called whenever user clicks search btn
    private void updateFilteredData() {
        //System.out.println("Calling updateFilteredData");
        filteredPlaylist.clear();

        //System.out.println("running matchesFilter");
        try {
            for (Playlist p : list) {
                if (matchesFilter(p)) {
                    filteredPlaylist.add(p);
                }
            }
        } catch (Exception e) {
            //System.out.println("EXCEPTION in updateFilteredData");
        }
        //System.out.println("completed running matchesFilter");
        //System.out.println("returning from updateFilteredData");
    }

    // returns TRUE if a matching playlist name was found. Used by updateFilteredData()
    private boolean matchesFilter(Playlist playlist) {
        String filterString = txtSearch.getText();

        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }

        String lowerCaseFilterString = filterString.toLowerCase();

        try {
            if (playlist.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
                return true;
            } else if (playlist.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
                return true;
            }
        } catch (Exception e) {
            //System.out.println("EXCEPTION in matchesFilter!");
        }

        return false; // Does not match
    }

    // opens the track stage
    public void showTrackWindow(ActionEvent event) throws IOException {
        Launcher.showTrackWindow(event);
    }

}
