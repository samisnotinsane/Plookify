package account;

import account.PopupBoxes.PopupLabelButton;
import account.PopupBoxes.SingleLabelPopUp;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by tahnik on 14/03/2016.
 */
public class PaypalController implements Initializable{
    @FXML private TextField paypalEmail;
    @FXML private TextField paypalPassword;
    @FXML private Button paypalPay;
    @FXML private Button paypalBack;
    Button button = new Button();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paypalBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane pane = PaymentStage.getPaymentGrid();
                TranslateTransition ff = new TranslateTransition(Duration.millis(1000), pane);
                ff.setByY(768);
                ff.play();
            }
        });
        paypalPay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(payment()) {
                    Utilities.removePayment();
                    Paypal paypal = new Paypal(paypalEmail.getText(), paypalPassword.getText());
                    paypal.makePayment();
                    common.ComponentLoader.getInstance().displaySubscribed();
                    Stage stage = (Stage) paypalPay.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }
    private boolean payment(){
        paypalEmail.setText(Utilities.escapeString(paypalEmail.getText()));
        paypalPassword.setText(Utilities.escapeString(paypalPassword.getText()));
        if(!paypalEmail.getText().contains("@") || !paypalEmail.getText().contains(".") || Utilities.checkIfStringIsEmpty(paypalEmail.getText())){
            new SingleLabelPopUp((Stage)paypalEmail.getScene().getWindow(), "Please enter a valid email");
            return false;
        }
        if(paypalPassword.getText().length() < 8){
            new SingleLabelPopUp((Stage) paypalEmail.getScene().getWindow(), "Password must be at least 8 Characters");
            return false;
        }
        return true;
    }
}
