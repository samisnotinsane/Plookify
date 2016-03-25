package common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import player.FXMLDocumentController;
import radio.GUIController;
import radio.GUIFindArtistController;

import java.io.IOException;

/**
 * Created by Kristian on 14/03/16.
 */
public class ComponentLoader {

    @FXML private Pane middlePane;
    @FXML private Pane rightSidePane;
    @FXML private Button radioBtn;

    private static ComponentLoader ourInstance = new ComponentLoader();

    public static ComponentLoader getInstance() {
        return ourInstance;
    }

    private ComponentLoader() {
    }

    public FXMLLoader loadFXML(String path){
        middlePane.getChildren().clear();
        FXMLLoader load = new FXMLLoader(getClass().getResource(path));
        try {
            middlePane.getChildren().add(load.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return load;
    }

    public FXMLLoader loadFXML(String path, FXMLDocumentController controller){
        middlePane.getChildren().clear();
        FXMLLoader load = new FXMLLoader(getClass().getResource(path));
        load.setController(controller);
        try {
            middlePane.getChildren().add(load.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return load;
    }

    public FXMLLoader loadFXML(String path, GUIFindArtistController controller){
        middlePane.getChildren().clear();
        FXMLLoader load = new FXMLLoader(getClass().getResource(path));
        load.setController(controller);
        try {
            middlePane.getChildren().add(load.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return load;
    }
    public FXMLLoader loadFXML(String path, GUIController controller){
        middlePane.getChildren().clear();
        FXMLLoader load = new FXMLLoader(getClass().getResource(path));
        load.setController(controller);
        try {
            middlePane.getChildren().add(load.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return load;
    }

    public void displaySubscribed(){
        try {
            String path = "/social/gui/Social3.fxml";
            radioBtn.setVisible(true);
            rightSidePane.getChildren().clear();
            FXMLLoader load = new FXMLLoader(getClass().getResource(path));
            try {
                rightSidePane.getChildren().add(load.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch(Exception e){

        }
    }

    public void hideSubscribed(){
        try {
            rightSidePane.getChildren().clear();
            radioBtn.setVisible(false);
        }catch (Exception e){
            //
        }
    }

    public void sendComponents(Pane middlePane, Pane rightSidePane, Button radioBtn){
        this.middlePane = middlePane;
        this.rightSidePane = rightSidePane;
        this.radioBtn = radioBtn;
    }
}
