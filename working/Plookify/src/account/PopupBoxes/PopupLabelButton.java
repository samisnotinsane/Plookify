package account.PopupBoxes;

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
 * Created by tahnik on 17/03/2016.
 */
public class PopupLabelButton extends Stage{
    Label label;
    VBox vBox;
    public PopupLabelButton(Stage stage, String message, ArrayList<Button> buttons){
        //Creating a new stage for popup menu
        this.setWidth(700);
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
        for(Button button : buttons){
            button.setStyle("-fx-border: 10;");
            button.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode().toString().equals("ENTER")){
                        ((Stage) button.getScene().getWindow()).close();
                    }
                }
            });
            vBox.getChildren().add(button);
        }

        this.setScene(new Scene(vBox));
        this.setAlwaysOnTop(true);
        this.initModality(Modality.APPLICATION_MODAL);
        this.showAndWait();
    }
}
