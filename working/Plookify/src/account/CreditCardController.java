package account;

import account.PopupBoxes.SingleLabelPopUp;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by tahnik on 14/03/2016.
 * This controller is responsible for validating the credit card details
 */
public class CreditCardController implements Initializable{
    @FXML private TextField creditCardName;
    @FXML private TextField creditCardLongDigit;
    @FXML private TextField creditCardCSV;
    @FXML private TextField creditCardExpiryMonth;
    @FXML private TextField creditCardExpiryYear;
    @FXML private Button creditCardPay;
    @FXML private Button creditCardBack;

    private int creditCardExpiry;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creditCardPay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(payment()) {
                    Utilities.removePayment();
                    CreditCard creditCard = new CreditCard(
                            Long.parseLong(creditCardLongDigit.getText()),
                            Integer.parseInt(creditCardCSV.getText()),
                            creditCardExpiry);
                    creditCard.makePayment();
                    common.ComponentLoader.getInstance().displaySubscribed();
                    Stage stage = (Stage) creditCardPay.getScene().getWindow();
                    stage.close();
                }
            }
        });
        creditCardBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane pane = PaymentStage.getPaymentGrid();
                TranslateTransition ff = new TranslateTransition(Duration.millis(1000), pane);
                ff.setByY(768);
                ff.play();
            }
        });
    }
    public boolean payment(){
        creditCardName.setText(Utilities.escapeString(creditCardName.getText()));
        creditCardLongDigit.setText(Utilities.escapeString(creditCardLongDigit.getText()));
        creditCardCSV.setText(Utilities.escapeString(creditCardCSV.getText()));
        creditCardExpiryMonth.setText(Utilities.escapeString(creditCardExpiryMonth.getText()));
        creditCardExpiryYear.setText(Utilities.escapeString(creditCardExpiryYear.getText()));
        if(creditCardLongDigit.getText().length() != 16){
            new SingleLabelPopUp((Stage) creditCardName.getScene().getWindow(), "Long Digit must have 16 Character");
            return false;
        }
        try {
            Long.parseLong(creditCardLongDigit.getText());
        }catch (NumberFormatException e){
            new SingleLabelPopUp((Stage) creditCardName.getScene().getWindow(), "You must enter only numbers in Long Digit Field");
            return false;
        }
        if(creditCardCSV.getText().length() != 3){
            new SingleLabelPopUp((Stage)creditCardName.getScene().getWindow(), "CSV must have only 3 Characters");
            return false;
        }
        try {
            Long.parseLong(creditCardCSV.getText());
        }catch (NumberFormatException e){
            new SingleLabelPopUp((Stage) creditCardName.getScene().getWindow(), "You must enter only numbers in CSV Field");
            return false;
        }
        if(creditCardExpiryMonth.getText().length() != 2){
            new SingleLabelPopUp((Stage)creditCardName.getScene().getWindow(), "Month must have only 2 Characters");
            return false;
        }
        try {
            Long.parseLong(creditCardExpiryMonth.getText());
        }catch (NumberFormatException e){
            new SingleLabelPopUp((Stage) creditCardName.getScene().getWindow(), "You must enter only numbers in Month Field");
            return false;
        }
        if(creditCardExpiryYear.getText().length() != 4){
            new SingleLabelPopUp((Stage)creditCardName.getScene().getWindow(), "Year must have only 4 Characters");
            return false;
        }
        try {
            Long.parseLong(creditCardExpiryYear.getText());
        }catch (NumberFormatException e){
            new SingleLabelPopUp((Stage) creditCardName.getScene().getWindow(), "You must enter only numbers in Year Field");
            return false;
        }
        return true;
    }
}
