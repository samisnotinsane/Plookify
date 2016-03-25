package playlist.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import playlist.Launcher;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by sameenislam on 12/03/2016.
 */
public class LoginController implements Initializable {
    @FXML
    public Label lblAuthStatus;
    @FXML
    public Button btnLogin;
    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField pwdLogin;

    String username, password;

    @FXML
    public void initialiseLogin(javafx.event.ActionEvent event) throws IOException {
        username = txtUsername.getText();
        password = pwdLogin.getText();
        if(username.equals("sam@sam.com") && password.equals("root") ) {
            System.out.println("Logged in");
            Launcher.showHome(event);
        } else {
            lblAuthStatus.setText("Invalid username/password!");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Login initialised from package: playlist.view");
    }
}
