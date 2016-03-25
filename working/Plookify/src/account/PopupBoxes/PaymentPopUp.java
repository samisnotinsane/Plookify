package account.PopupBoxes;

import account.PaymentStage;
import javafx.geometry.Rectangle2D;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
 * Created by tahnik on 24/03/2016.
 */
public class PaymentPopUp extends Stage{
    Label label;
    VBox vBox;
    Button button = new Button();
    Button makePayment = new Button();
    public PaymentPopUp(){
        //Creating a new stage for popup menu
        this.setWidth(500);
        this.setHeight(200);
        Rectangle2D screenRes = Screen.getPrimary().getVisualBounds();
        //Centering the popup menu in the main stage
        double x = 0 + screenRes.getWidth()/2 - this.getWidth()/2;
        double y = 0 + screenRes.getHeight()/2 - this.getHeight()/2;
        this.setX(x);
        this.setY(y);

        //adding the label and button for the popup menu
        label = new Label("Your payment is now due. \nPlease select a subscription type or your account will be reverted to free");
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
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
        makePayment.setText("Select Subscription Type");
        makePayment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    closeStage();
                    new PaymentStage();
                }catch (Exception e){
                    //
                }
            }
        });
        vBox.getChildren().add(makePayment);
        vBox.getChildren().add(button);

        this.setScene(new Scene(vBox));
        this.setAlwaysOnTop(true);
        //this.initModality(Modality.APPLICATION_MODAL);
        this.show();
    }
    public void closeStage(){
        this.close();
    }
}
