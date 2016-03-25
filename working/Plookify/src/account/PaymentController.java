package account;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by tahnik on 15/03/2016.
 */
public class PaymentController implements Initializable{
    @FXML private HBox paymentPaypal;
    @FXML private HBox paymentCreditCard;
    @FXML private Button paypalBack;
    @FXML private HBox paymentFree;

    BorderPane CreditCard;
    BorderPane Paypal;
    GridPane pane;
    TranslateTransition ff;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader paypalfx = new FXMLLoader(getClass().getResource("Paypal.fxml"));
        PaypalController paypalController = new PaypalController();
        paypalfx.setController(paypalController);
        Paypal = null;
        try {
            Paypal = (BorderPane) paypalfx.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        FXMLLoader creditcardfx = new FXMLLoader(getClass().getResource("CreditCard.fxml"));
        CreditCardController creditCardController = new CreditCardController();
        creditcardfx.setController(creditCardController);
        CreditCard = null;
        try {
            CreditCard = (BorderPane) creditcardfx.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        paymentPaypal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                pane = PaymentStage.getPaymentGrid();
                pane.getChildren().remove(CreditCard);
                pane.getChildren().remove(Paypal);
                pane.add(Paypal, 0, 1);
                ff = new TranslateTransition(Duration.millis(800), pane);
                ff.setByY(-768);
                ff.play();
            }
        });
        paymentFree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String singleQuery = "UPDATE USERSUBSCRIPTION SET SUBSTYPE='" + 0 + "' WHERE USERID='" +
                        Utilities.getUserID(Authenticator.getUser().getEmail()) + "'";
                //System.out.println(Utilities.getUserID(Authenticator.getUser().getEmail()));
                Utilities.executeSingleQuery(singleQuery);
                Authenticator.getUser().setSubscriptionType(false);
                if(Utilities.paymentExists()){
                    Utilities.removePayment();
                }
                common.ComponentLoader.getInstance().hideSubscribed();
                ((Stage) paymentFree.getScene().getWindow()).close();
            }
        });
        paymentCreditCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pane = PaymentStage.getPaymentGrid();
                pane.getChildren().remove(Paypal);
                pane.getChildren().remove(CreditCard);
                pane.add(CreditCard, 0, 1);
                ff = new TranslateTransition(Duration.millis(800), pane);
                ff.setByY(-768);
                ff.play();
            }
        });
    }
}
