package radio;

import account.Authenticator;
import account.Utilities;
import common.ComponentLoader;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import playlist.Playlist;
import playlist.Track;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by nicks on 01/03/2016.
 */
public class GUIController implements Initializable {
    private ObservableList<Songs> data;
    private ArrayList<String> results;
    @FXML
    private TableView resultsRadio;
    @FXML
    private TableColumn artistName;
    @FXML
    private TableColumn song;
    @FXML
    private TableColumn genre;
    @FXML
    private TableColumn time;
    private Radio query;
    @FXML
    private TextField artistEntry;
    private String artist;


    public void getArtist(String artist)
    {
        this.artist = artist;
        generateRadio();
    }
    public void initData(Radio query) {
        this.query = query;
        data = query.convertToObservableList();
        resultsRadio.setItems(data);
        artistName.setCellValueFactory((new PropertyValueFactory<Songs, String>("trackArtist")));
        song.setCellValueFactory((new PropertyValueFactory<Songs, String>("trackName")));
        genre.setCellValueFactory((new PropertyValueFactory<Songs, String>("trackGenre")));
        time.setCellValueFactory((new PropertyValueFactory<Songs, String>("trackTime")));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        resultsRadio.setPlaceholder(new Label("There are no similar artists for the artist you have searched for"));
        artistEntry.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    String artistSearch = artistEntry.getText();
                    try {
                        changeSceneToFindArtist(artistSearch);
                    }
                    catch(Exception e)
                    {
                        System.out.println("error in radio module");
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @FXML
    private void handleSavePlaylistButtonAction(ActionEvent event) throws IOException {
        newPlaylist();
    }

    @FXML
    private void handleArtistEntryButtonAction(ActionEvent event) throws IOException {
        String artistSearch = artistEntry.getText();
        changeSceneToFindArtist(artistSearch);

    }

    private void changeSceneToFindArtist(String artist) throws IOException{
        FXMLLoader load = ComponentLoader.getInstance().loadFXML("/radio/GUIFindArtist.fxml",new GUIFindArtistController());
        GUIFindArtistController controller = load.getController();
        controller.initData(artist);

    }

    private void generateRadio() {
        Radio query = new Radio();
        query.search(artist);
        query.randomGenerator();
        FXMLLoader load = ComponentLoader.getInstance().loadFXML("/radio/GUI.fxml", new GUIController());
        GUIController controller = load.getController();
        controller.initData(query);
    }

    private void newPlaylist() {
        String playlistname = "";
        if (resultsRadio.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No radio generated!");
            alert.setContentText("You have not generated a radiot");

            alert.showAndWait();

        } else {
            TextInputDialog dialog = new TextInputDialog("New Radio Playlist");
            dialog.setTitle("Create New Playlist");
            dialog.setHeaderText("New playlist");
            dialog.setContentText("Please enter a name for your playlist:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Nothing entered!");
                    alert.setContentText("You didn't enter a name for your playlist");

                    alert.showAndWait();
                } else {
                    playlistname = result.get();
                    convertSongsToTrack(playlistname);
                }
            }
        }
    }

    private void convertSongsToTrack(String playlistname)
    {

        int userid = Utilities.getUserID(Authenticator.getUser().getEmail());
        Playlist radio = new Playlist(playlistname,userid,"private");
        for(int i = 0; i < resultsRadio.getItems().size(); i++)
        {
            Songs song = (Songs) resultsRadio.getItems().get(i);
            Track tr = new Track(Integer.parseInt(song.getTrackID()),song.getTrackName(),song.getTrackArtist(),convertMinutesToSeconds(song.getTrackTime()));
            radio.addTrack(tr);
        }
    }
    private int convertMinutesToSeconds(String duration)
    {
        int seconds = Integer.parseInt(duration.split(":")[1])+ (60 * Integer.parseInt(duration.split(":")[0])) ;
        return seconds;
    }
}
