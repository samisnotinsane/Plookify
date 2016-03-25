package common;

import account.Authenticator;
import account.LoginStage;
import account.Utilities;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import player.FXMLDocumentController;
import radio.GUIFindArtistController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Kristian on 03/03/16.
 */
public class commonGUIController implements Initializable {

    @FXML private Pane middlePane;
    @FXML private Pane bottomPane;
    @FXML private Pane rightSidePane;
    @FXML private Button radioBtn;
    @FXML private Button logout;

    private FXMLDocumentController controller;
    private ComponentLoader loader = ComponentLoader.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadPlayerPane();
            //loadSocial();
            loader.sendComponents(middlePane, rightSidePane, radioBtn);
            if(!Authenticator.getUser().getSubscriptionType()){
                ComponentLoader.getInstance().hideSubscribed();
            }
            else{
                ComponentLoader.getInstance().displaySubscribed();
            }
            loadPlayer();
            logout.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Authenticator.logout();
                    FXMLDocumentController.stopTP();
                    ((Stage) logout.getScene().getWindow()).close();
                    try {
                        new LoginStage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerPane() throws IOException {

        FXMLLoader load = new FXMLLoader(getClass().getResource("/player/mainPlayerGUI.fxml"));
        bottomPane.getChildren().add(load.load());
        controller = load.<FXMLDocumentController>getController();
    }

    public void loadPlayer() throws IOException {
        FXMLLoader load = loader.loadFXML("/player/secondaryPlayerGUI.fxml", controller);
    }

    public void loadSocial() throws IOException {
        //loader.loadFXML("../social/gui/Social.fxml");
            rightSidePane.getChildren().clear();
            FXMLLoader load = new FXMLLoader(getClass().getResource("/social/gui/Social3.fxml"));
            rightSidePane.getChildren().add(load.load());
    }

    public void loadPlaylist(){
        loader.loadFXML("/playlist/view/Main.fxml");
    }

    public void loadRadio(){
        loader.loadFXML("/radio/GUIFindArtist.fxml",new GUIFindArtistController());
    }

    public void loadAccount(){
        loader.loadFXML("/account/settings.fxml");
    }


    public void removePane() {
        middlePane.getChildren().clear();
    }

    public void mainPaneKey(KeyEvent event){
        if(event.getCode() == KeyCode.SPACE){
            controller.mainPaneKey(event);
        }
    }
}
