package playlist.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by sameenislam on 13/03/2016.
 */
public class MainPlaylistViewController implements Initializable {



    @FXML public TableView<Playlist> tblPlaylists = new TableView<>();
    @FXML public TableView tblTracks;
    @FXML public TextField txtPlaylistName;

    String txtPlPlaceholder = "No Playlists :/\n" +
            "Why not add one?";
    String txtTrkPlaceholder = "No tracks in library.";
    private final ObservableList<Playlist> oblPlaylists =
            FXCollections.observableArrayList(
                    new Playlist("Running"),
                    new Playlist("CodeJams"),
                    new Playlist("Accoustic"),
                    new Playlist("Fall Out Boys"),
                    new Playlist("Favorites")
            );

    // pulls data from db again
    public void refresh() {
        System.out.print("Refreshing MainPlaylistView...");
        try {
            System.out.print(" OK");
        } catch (Exception e) {
            System.out.print(" FAIL");
        }

    }

    @FXML
    public void createPlaylistHeader(ActionEvent event) {
        ObservableList<Playlist> data = tblPlaylists.getItems();
        data.add(new Playlist(txtPlaylistName.getText()));
        txtPlaylistName.setText("");

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // runs upon loading the scene
        System.out.println("MainPlaylistViewController initialised");

        tblPlaylists.setEditable(true);
        tblTracks.setEditable(false);

        tblPlaylists.setPlaceholder(new Label(txtPlPlaceholder));
        tblTracks.setPlaceholder(new Label(txtTrkPlaceholder));

        TableColumn<Playlist, String> playlistsCol =
                new TableColumn<>("Playlists (vN)");
        playlistsCol.setMinWidth(100);
        playlistsCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        playlistsCol.setCellFactory(TextFieldTableCell.<Playlist>forTableColumn());
        playlistsCol.setOnEditCommit(
                (TableColumn.CellEditEvent<Playlist, String> t) -> {
                    ((Playlist) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
                });

//        TableColumn playlistsCol = new TableColumn("Playlists (vN)");
//        playlistsCol.setMinWidth(100);
//        playlistsCol.setCellValueFactory(
//                new PropertyValueFactory<>("playlistName")
//        );
        tblPlaylists.setItems(oblPlaylists);
        tblPlaylists.getColumns().addAll(playlistsCol);

        TableColumn trkNumCol = new TableColumn("# (vN)");
        playlistsCol.setMinWidth(50);
        playlistsCol.setCellValueFactory(
                new PropertyValueFactory<>("trackSequenceNo")
        );
        tblTracks.setItems(oblPlaylists);
        tblTracks.getColumns().addAll(trkNumCol);



    }

    public static class Playlist {
        public final SimpleStringProperty playlistName = new SimpleStringProperty("");
        private String name = "Hello";

        public Playlist() {
            this("");
        }
        private Playlist(String plName) {
            setName(plName);
            //this.playlistName = new SimpleStringProperty(plName);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            playlistName.set(name);
        }

    }
}
