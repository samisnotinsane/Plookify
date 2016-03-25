package account;

import account.PopupBoxes.PopupLabelButton;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by tahnik on 15/03/2016.
 */
public class LoginController implements Initializable {
    @FXML private Button login;
    @FXML private TextField username_login;
    @FXML private TextField password_login;
    @FXML private Button register_login;
    TranslateTransition ff = new TranslateTransition();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginUser();
            }
        });
        register_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ff.setNode(register_login.getParent().getParent().getParent().getParent());
                ff.setByX(-1024);
                ff.setDuration(Duration.millis(700));
                ff.play();
            }
        });
        password_login.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString().equals("ENTER")){
                    loginUser();
                }
            }
        });
        username_login.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString().equals("ENTER")){
                    loginUser();
                }
            }
        });
    }
    private void loginUser(){
        username_login.setText(Utilities.escapeString(username_login.getText()));
        password_login.setText(Utilities.escapeString(password_login.getText()));
        username_login.setText(username_login.getText().toLowerCase());
        if(Authenticator.getUser() != null){
            System.out.println("User already logged in");
        }else{
            Authenticator authenticator = new Authenticator(username_login.getText(), password_login.getText());
            if(!authenticator.Authenticate()){
                Button button = new Button("Close");
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        ((Stage) button.getScene().getWindow()).close();
                    }
                });
                ArrayList<Button> buttons = new ArrayList<>();
                buttons.add(button);
                new PopupLabelButton((Stage) login.getScene().getWindow(), "Wrong username or password", buttons);
            }
            try {
                if (!Authenticator.getUser().getStatus()) {
                    Button button = new Button("Close");
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ((Stage) button.getScene().getWindow()).close();
                            Authenticator.logout();
                        }
                    });
                    Button button1 = new Button("Open Account");
                    button1.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String singleQuery;
                            singleQuery = "UPDATE USERSTATUS SET STATUS='" + 1 + "' WHERE USERID='" +
                                    Utilities.getUserID(Authenticator.getUser().getEmail()) + "'";
                            Utilities.executeSingleQuery(singleQuery);
                            ((Stage) button1.getScene().getWindow()).close();
                        }
                    });
                    ArrayList<Button> buttons = new ArrayList<Button>();
                    buttons.add(button);
                    buttons.add(button1);
                    new PopupLabelButton((Stage) login.getScene().getWindow(), "Sorry you have closed your account", buttons);
                }
            }catch (NullPointerException e){

            }
        }
        if(Authenticator.getUser() != null){
            Stage primary = (Stage) login.getScene().getWindow();
            primary.close();
            try {
                new common.MainGUI();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
