package social.gui;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import social.logic.GUImthds;
import social.logic.database;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


// javafx main class to control
public class MainConFX implements Initializable {

    // @FXML
    // private Button nawaz = new Button();
    // @FXML
    // private Button muadh = new Button();

    //User user= new User();


    @FXML
    public ListView friendslst = new ListView();

    /*@FXML
    Checkbox dischk = new Checkbox();
*/

    @FXML
    CheckBox dischk = new CheckBox();

    @FXML
    public ComboBox requests = new ComboBox();

    @FXML
    Button searchbtn = new Button();

    /**
     * Getter for property 'friends'.
     *
     * @return Value for property 'friends'.
     */
    public ObservableList<String> getFriends() {
        return friends;
    }

    /**
     * Setter for property 'friends'.
     *
     * @param friends Value to set for property 'friends'.
     */
    /*public void setFriends(ObservableList<String> friends) {
        this.friends.setAll(friends);
    }*/

    private ObservableList<String> friends = FXCollections.observableArrayList();
    private ObservableList<String> requestobs = FXCollections.observableArrayList();
    social.logic.database dbcon = new database();
    social.logic.GUImthds GUIM = new GUImthds();
   PlaylistCon playlist = new PlaylistCon();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        friends = dbcon.Ufriends();
        friendslst.setItems(friends);
        GUIM.setDbcon1ss(dbcon, this);
      //  playlist.setDbcon1ss(dbcon, this);

            requestobs=dbcon.Frequests();
        requests.setItems(requestobs);
        /*requests.setOnMouseClicked(eventt -> {
            String Reqname = requests.getSelectionModel().getSelectedItem().toString();
            GUIM.ifaccept(Reqname);
        });*/
        // pop up to accept or decline friends request which is in GUImthds
        requests.setOnAction(new javafx.event.EventHandler() {
            //@Override
            public void handle(Event t) {
        try {
            String Reqname = requests.getValue().toString();
            int temp = Integer.parseInt(Reqname.replaceAll("[\\D]", ""));
            //System.out.println(temp);
            GUIM.ifaccept(temp, Reqname);
        }
        catch(Exception e){}
            }

        });

        // nawaz.setText("heyyloooo");
        // muadh.setText("beyyeyey");
        // FriendsList ff = new FriendsList();

        //

        //


        //when clicked on private
        //int tempid= 2;

        //if(privchk.ifSelected())
        //db.privmthd(tempid);       // if clicked

        //friends = FXCollections.observableArrayList();
        /*database db= new database();
       database.Ufriends();
        logic.database = new logic.database();
        logic.*/


        //friends.addAll( db11.Ufriends());
        //System.out.print(db11.Ufriends());


        //friendslst.setItems(friends);


        // friends.addAll(tempfriends);
        //friendslst.setItems(friends);

        // to get the name when a user clicks
        friendslst.setOnMouseClicked(event -> {
            try {
                String Rname = friendslst.getSelectionModel().getSelectedItem().toString();
                GUIM.dialog(Rname);
            }
            catch(Exception e){}

            // System.out.println(Rname);
            //String Rname =

        });


        /*friendslst.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
               // friendslst.setItems(dbcon.Ufriends());
            }
        });*/

       /* friendslst.setOnMouseClicked(new EventHandler< javafx.event.Event>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + friendslst.getSelectionModel().getSelectedItem());
            }
        });*/


       /* friendslst.setOnAction(new javafx.event.EventHandler() {
            //@Override
            public void handle(javafx.event.Event t) {

               // Weather.setFont(fonts.getValue().toString());
            }

        });*/

        int isdis = dbcon.ischecked();
        if (isdis == 1)
            dischk.setSelected(true);


//       dischk.setOnAction((event) -> {
//           if(dischk.isSelected());
//           dbcon.privmthd();
//        });

        dischk.setOnAction((event) -> {
            if (dischk.isSelected()) ;
            dbcon.privmthd1();
        });

        dischk.setOnAction((event) -> {
            if (!dischk.isSelected()) ;
            dbcon.notprivmthd1();
        });



       /* EventHandler eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox dischk = (CheckBox) event.getSource();
                    System.out.println("Action performed on checkbox " + dischk.getText());
                    if ("dischk".equals(dischk.getText())) {
                       // dischk.setSelected(!chk1.isSelected());
                    } else if ("chk 2".equals(chk.getText())) {
                        chk1.setSelected(!chk2.isSelected());
                    }
                }
            }
        };*/


        searchbtn.setOnAction((event) -> {

            GUIM.enterName();
        });


    }

    public void delete(String person) {
        friends.remove(person);
        friendslst.setItems(friends);

    }
    public void delete1(String person) {
        requestobs.remove(person);
        requests.setItems(requestobs);

    }



}

