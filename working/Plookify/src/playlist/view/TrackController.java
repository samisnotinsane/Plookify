package playlist.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import playlist.Launcher;
import playlist.Playlist;
import playlist.PlaylistManager;
import playlist.Track;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by sameenislam on 22/03/2016.
 */
public class TrackController implements Initializable {

    @FXML
    TextField txtSearch;
    @FXML
    Button btnSearch;
    @FXML
    Button btnTrack;
    @FXML
    Button btnAddToPlaylist;
    @FXML
    TableView<Track> tblTrack;
    @FXML
    TableColumn colTrack;
    @FXML
    TableColumn colArtist;
    @FXML
    TableColumn colDuration;

    // will hold all the tracks
    @FXML ObservableList<Track> trackList = FXCollections.observableArrayList();
    ObservableList<Track> filteredTrackList = FXCollections.observableArrayList();

    private Track selectedTrack;


    // constr. called before initialize()
    public TrackController() {
//        filteredTrackList.addAll(trackList);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialiseTableView();
        getTableViewData();
        try {
            populateTableView();
        } catch (Exception e) {
            //System.out.println("An error occurred while attempting to populate table!");
        }


    }


    @FXML
    private void addTrackToPlaylist(ActionEvent event) {
        // get selected track from tableview
        //System.out.println("Adding selected track to playlist");
        PlaylistStore.getInstance().getPlaylist().addTrack(tblTrack.getSelectionModel().getSelectedItem());
        //get selected playlist object from the listview in main scene


    }

    // prepare table for population
    public void initialiseTableView() {
        // shown by default upon loading and when playlist is empty
        Label placeholderLabel = new Label("System error! Unable to load Tracks, please go back and try again.");
        VBox placeholderLayout = new VBox(10);
        placeholderLayout.setPadding(new Insets(200,100,100,160));
        placeholderLayout.getChildren().add(placeholderLabel);
        tblTrack.setPlaceholder(placeholderLayout);
        tblTrack.setEditable(true);
    }

    // fetches tracks from the database
    public void getTableViewData() {
        // have a Track function that uses SQL class to get data from TRACK table
        Track.buildData();
        Track.generateObjects();
        trackList = Track.getAllTracks();

    }

    // populates columns with the correct data
    private void populateTableView() throws Exception {


        colTrack.setCellValueFactory(new PropertyValueFactory<Track, String>("name"));
        colArtist.setCellValueFactory(new PropertyValueFactory<Track, String>("artist"));
        colDuration.setCellValueFactory(new PropertyValueFactory<Track, Integer>("duration"));

        // trackList is a list of track objects
        tblTrack.setItems(trackList);
        selectionMonitor();

    }


    private void selectionMonitor() {
        MultipleSelectionModel<Track> lvSelModel = tblTrack.getSelectionModel();
        // changelistener for monitoring change of selection with the listview
        lvSelModel.selectedItemProperty().addListener(new ChangeListener<Track>() {
            @Override
            public void changed(ObservableValue<? extends Track> observable, Track oldValue, Track newValue) {

                // oldValue is the previous selection,
                // newValue is the new selection (text)

                selectedTrack = newValue;
                //System.out.println("track loaded: "+newValue);

            }
        });
    }

    public void showMainWindow(ActionEvent event) throws IOException {
        Launcher.showHome(event);
    }

    public void showTrackWindow(ActionEvent event) throws IOException {
        Launcher.showTrackWindow(event);
    }


}
