package account;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.crypto.dsig.TransformService;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by tahnik on 13/03/2016.
 */
public class PaymentStage extends Stage{
    @FXML private HBox paymentPaypal;
    @FXML private Button paypalBack;


    GridPane Payment;
    static BorderPane Paypal;
    BorderPane CreditCard;

    Scene scene;
    static GridPane PaymentGrid;

    public PaymentStage() throws Exception{
        FXMLLoader Paymentfx = new FXMLLoader(getClass().getResource("Payment.fxml"));
        FXMLLoader Paypalfx = new FXMLLoader(getClass().getResource("Paypal.fxml"));
        FXMLLoader CreditCardfx = new FXMLLoader(getClass().getResource("CreditCard.fxml"));

        PaymentController paymentController = new PaymentController();
        PaypalController paypalController = new PaypalController();
        CreditCardController creditCardController = new CreditCardController();

        Paymentfx.setController(paymentController);
        Paypalfx.setController(paypalController);
        CreditCardfx.setController(creditCardController);

        Payment = (GridPane) Paymentfx.load();
        Paypal = (BorderPane) Paypalfx.load();
        CreditCard = (BorderPane) CreditCardfx.load();

        Payment.setTranslateZ(0);
        Paypal.setTranslateZ(1);
        CreditCard.setTranslateZ(2);

        PaymentGrid = new GridPane();

        PaymentGrid.getColumnConstraints().add(new ColumnConstraints(1024));
        PaymentGrid.getRowConstraints().add(new RowConstraints(768));
        PaymentGrid.getRowConstraints().add(new RowConstraints(768));

        PaymentGrid.add(Payment, 0, 0);

        scene = new Scene(PaymentGrid, 1024, 768);

        this.setScene(scene);
        this.setTitle("Payment");
        this.initModality(Modality.APPLICATION_MODAL);
        this.showAndWait();
    }

    protected static GridPane getPaymentGrid(){
        return PaymentGrid;
    }

    protected static Pane getPaypal(){
        return Paypal;
    }

}
