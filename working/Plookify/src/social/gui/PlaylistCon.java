package social.gui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import social.logic.database;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by k1326476 on 20/03/2016.
 */
public class PlaylistCon implements Initializable {

    social.logic.database dbcon ;
    int userID = 0;


@FXML
   ListView playlist = new ListView();

    @FXML
    ListView tracks = new ListView();





    /** {@inheritDoc} */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {











    }

    public void getPlaylistInfo()
    {

        playlist.setItems(dbcon.Fplaylist(userID));

        playlist.setOnMouseClicked(new javafx.event.EventHandler() {
            int temp=0;
            //@Override
            public void handle(Event t) {

                try {
                    String Pname = playlist.getSelectionModel().getSelectedItem().toString();
                     temp = Integer.parseInt(Pname.replaceAll("[\\D]", ""));
                    //System.out.println(temp);
                    //dbcon.Ftracks(temp);
                    try {

                        tracks.setItems(dbcon.Ftracks(temp));

                    }
                    catch(Exception e){}
                }
                catch(Exception e){}
            }


        });



    }

    public void setDbcon(database dbcon) {
        this.dbcon = dbcon;
    }

    /**
     * Setter for property 'userID'.
     *
     * @param userID Value to set for property 'userID'.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }





}
