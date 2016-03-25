package account.PopupBoxes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by tahnik on 23/03/2016.
 */
public class SingleLabelPopUp extends Stage{
    Label label;
    VBox vBox;
    Button button = new Button();
    public SingleLabelPopUp(Stage stage, String message){
        //Creating a new stage for popup menu
        this.setWidth(500);
        this.setHeight(200);
        //Centering the popup menu in the main stage
        double x = stage.getX() + stage.getWidth()/2 - this.getWidth()/2;
        double y = stage.getY() + stage.getHeight()/2 - this.getHeight()/2;
        this.setX(x);
        this.setY(y);

        //adding the label and button for the popup menu
        label = new Label(message);
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(10));

        //VBox for the popup menu
        vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(label);
        button.setText("Close");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Stage) button.getScene().getWindow()).close();
            }
        });
        vBox.getChildren().add(button);

        this.setScene(new Scene(vBox));
        this.setAlwaysOnTop(true);
        this.initModality(Modality.APPLICATION_MODAL);
        this.showAndWait();
    }
}
