package social.logic;

import common.ComponentLoader;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import social.gui.MainConFX;
import social.gui.PlaylistCon;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ec14167 on 10/03/2016.
 */
public class GUImthds {
    /* fixed file name */
    MainConFX main;
    database dbcon1ss;

    /**
     * Setter for property 'dbcon1ss'.
     *
     * @param dbcon1ss Value to set for property 'dbcon1ss'.
     */
    // main method..
    public void setDbcon1ss(database dbcon1ss,MainConFX main) {
        this.dbcon1ss = dbcon1ss;
        this.main = main;
    }

    public void dialog(String Rname) {

        int Fid=Integer.parseInt(Rname.replaceAll("[\\D]", ""));

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog with Custom Actions");
        alert.setHeaderText("Choose one of the options below for " + Rname);
        alert.setContentText("Choose your option.");

        ButtonType playlist = new ButtonType("See playlist");
        ButtonType Rfriends = new ButtonType("Remove Friends");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        //ButtonType buttonTypeThree = new ButtonType("Three");
        // ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(playlist, Rfriends,buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == playlist) {
            try {
                FXMLLoader load = ComponentLoader.getInstance().loadFXML("/social/gui/playlist_tracks.fxml");

                PlaylistCon con = load.getController();
                con.setDbcon(dbcon1ss);
                con.setUserID(Fid);
               con.getPlaylistInfo();
            } catch (Exception ex) {
                Logger.getLogger(GUImthds.class.getName()).log(Level.SEVERE, null, ex);
            }


            //call method
            // dbcon1ss.Fplaylist(Fid);

        } else if (result.get() == Rfriends) {
            String itemToRemove = (String) main.friendslst.getSelectionModel().getSelectedItem();
            dbcon1ss.removefriend(Fid);
            main.delete(itemToRemove);

        }
        else {

        }

    }


    // dialog box to ask the user to add or reject a friend..
    public void ifaccept(int Reqid, String Reqname) {


        String msg=dbcon1ss.getmsg(Reqid);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Friend request");
        alert.setHeaderText("Choose one of the options below for " + Reqname);
        alert.setContentText("Message sent by " + Reqname + ": " + msg);

        ButtonType Accept = new ButtonType("Accept");
        ButtonType Reject = new ButtonType("Decline");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        //ButtonType buttonTypeThree = new ButtonType("Three");
        // ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(Accept, Reject,buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == Accept) {
            dbcon1ss.addfriend(Reqid);
            String itemToRemove = (String) main.requests.getValue().toString();
           // dbcon1ss.removefriend(Reqname);
            main.delete1(itemToRemove);

        } else if (result.get() == Reject) {
           /* String itemToRemove = (String) main.friendslst.getSelectionModel().getSelectedItem();
            dbcon1ss.removefriend(Reqname);
            main.delete(itemToRemove);
*/           dbcon1ss.reject(Reqid);
            String itemToRemove = (String) main.requests.getValue().toString();
            main.delete1(itemToRemove);
        }
        else {

        }

    }


    public void enterName()
    {

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Search friend");
        dialog.setHeaderText("Enter the email address of your friend to search");
        dialog.setContentText("Please enter your friend's email address here:");
        new TextInputDialog("");

        Optional<String> result = dialog.showAndWait();

        //dbcon1ss.



        if (result.isPresent()){
            // System.out.println("Your name: " + result.get());
            confirmFriend(result.get());

        }

    }

    public void confirmFriend(String friendemail) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        //alert.setHeaderText("Please confirm if you are happy to add");
        alert.setContentText("Are you sure, you want to add " + friendemail + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            int ifpaid= dbcon1ss.checkPaid(friendemail);
            int ifdisc=dbcon1ss.isdisc(friendemail);

            int ifyes =dbcon1ss.sentfriends(friendemail);
            int ifyesA =dbcon1ss.alreadyfriends(friendemail);
            int Own =dbcon1ss.ownUser(friendemail);
            if(Own==1)
               Ofriend(friendemail);

            else if(ifyesA==1)
                Afriend(friendemail);
            else if (ifyes==1)
                Sfriend(friendemail);

            else if(ifpaid==0 | ifdisc==0)
                Cfind(friendemail);
            else
                msgNreq(friendemail);

        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    public void Ofriend(String friendemail){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Trying you add yourself!!!");
        alert.setContentText(" You cannot add yourself ");

        alert.showAndWait();

    }


    public void Sfriend(String friendemail){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Cannot send request to whom you have already sent");
        alert.setContentText(" You have already sent a request to " + friendemail);

        alert.showAndWait();

    }
    public void Afriend(String friendemail){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Already a friend");
        alert.setContentText(" You are already friends with " + friendemail);

        alert.showAndWait();

    }

//cant find dialog box
    public void Cfind(String friendemail){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("NOT FOUND");
        alert.setContentText(friendemail + " has not been found");

        alert.showAndWait();

    }



    public void msgNreq(String friendemail){

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Send request");
        dialog.setHeaderText("Enter the message you want to send to " + friendemail);
        dialog.setContentText("Please enter the message here:");
        new TextInputDialog("");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            dbcon1ss.sendrequest(friendemail, result.get());
        }


    }


}
