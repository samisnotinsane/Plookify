package radio;

import common.ComponentLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIFindArtistController implements Initializable {
    private String artist;
    @FXML
    private Button submitSearch;

    @FXML
    private TextField artistEntry;

    @FXML
    private Button submitPlaylist;

    @FXML
    private ListView<String> resultsRadio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchFor();
        resultsRadio.setPlaceholder(new Label("No artist listed"));
        artistEntry.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    artist = artistEntry.getText();
                    searchFor();
                }
            }
        });

    }
    void initData(String query)
    {
        artist = query;
        searchFor();
    }
    @FXML
    void handleArtistEntryButtonAction(ActionEvent event) {
        artist = artistEntry.getText();
        searchFor();

    }
    void searchFor()
    {
        Radio query = new Radio();
        ObservableList<String> names = FXCollections.observableList(query.searchArtist(artist));
        resultsRadio.setItems(names);
    }

    @FXML
    void handleGenerateRadioAction(ActionEvent event) {
        String artist = resultsRadio.getSelectionModel().getSelectedItems().get(0);
        FXMLLoader load = ComponentLoader.getInstance().loadFXML("/radio/GUI.fxml", new GUIController());
        GUIController controller = load.getController();
        controller.getArtist(artist);
    }}
