package account;

import account.PopupBoxes.ErrorPopUpLabel;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by tahnik on 15/03/2016.
 */
public class RegistrationController implements Initializable{
    @FXML
    private TextField registrationFirstName;
    @FXML private TextField registrationLastName;
    @FXML private TextField registrationAddress;
    @FXML private TextField registrationCity;
    @FXML private TextField registrationPostcode;
    @FXML private TextField registrationEmail;
    @FXML private TextField registrationPassword;
    @FXML private TextField registrationConfirmPassword;
    @FXML private Button register_back;
    @FXML private Button registrate;
    TranslateTransition ff = new TranslateTransition();
    ArrayList<TextField> registrationFields = new ArrayList<>();
    ArrayList<String> errors = new ArrayList<>();
    ArrayList<String> errorField = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registrate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(registration()) {
                    Stage stage = (Stage) registrate.getScene().getWindow();
                    stage.close();
                    try {
                        new PaymentStage();
                        new common.MainGUI();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        register_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ff.setNode(register_back.getParent().getParent().getParent().getParent());
                ff.setDuration(Duration.millis(700));
                ff.setByX(1024);
                ff.play();
            }
        });
    }
    private boolean registration(){
        errorField.add("First Name");
        errorField.add("Last Name");
        errorField.add("Address");
        errorField.add("City");
        errorField.add("Postcode");
        errorField.add("Email");
        errorField.add("Password");
        registrationFields.clear();
        registrationEmail.setText(registrationEmail.getText().toLowerCase());
        registrationFields.add(registrationFirstName);
        registrationFields.add(registrationLastName);
        registrationFields.add(registrationAddress);
        registrationFields.add(registrationCity);
        registrationFields.add(registrationPostcode);
        registrationFields.add(registrationEmail);
        registrationFields.add(registrationPassword);
        errors.clear();
        for(TextField tf: registrationFields){
            tf.setText(Utilities.escapeString(tf.getText()));
        }
        for(TextField tf: registrationFields){
            if(Utilities.checkIfStringIsEmpty(tf.getText())){
                errors.add(errorField.get(registrationFields.indexOf(tf)) + " field is empty");
            }
        }
        if(!errors.isEmpty()){
            new ErrorPopUpLabel((Stage) registrationFirstName.getScene().getWindow(), errors);
            return false;
        }
        errors.clear();
        if(registrationPostcode.getText().length() > 8){
            errors.add("Postcode is not valid");
        }
        if(!registrationEmail.getText().contains("@") || !registrationEmail.getText().contains(".")){
            errors.add("Email is not valid");
        }
        if(!registrationPassword.getText().equals(registrationConfirmPassword.getText())){
            errors.add("Password doesn't match");
        }
        if(registrationPassword.getText().length() < 6){
            errors.add("Password must be at least 6 characters");
        }
        if(!errors.isEmpty()){
            new ErrorPopUpLabel((Stage) registrationFirstName.getScene().getWindow(), errors);
            return false;
        }
        Registrar registrar = new Registrar();
        registrar.setFirstName(registrationFirstName.getText());
        registrar.setLastName(registrationLastName.getText());
        registrar.setAddressLine(registrationAddress.getText());
        registrar.setCity(registrationCity.getText());
        registrar.setPostCode(registrationPostcode.getText());
        registrar.setEmail(registrationEmail.getText());
        registrar.setPassword(registrationPassword.getText());
        registrar.registrate();
        return true;
    }
}
