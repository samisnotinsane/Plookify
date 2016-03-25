package playlist;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by sameenislam on 11/03/2016.
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

    public void initialiseLogin() {
        System.out.println("'Login' clicked");
        System.out.println("Supplied credentials: ");
        String login = txtUsername.getText();
        String pwd = pwdLogin.getText();
        System.out.print(login + "\n" + pwd + "\n");
        if(User.authenticate(login,pwd)) {
            System.out.println("Authentication successful");
            lblAuthStatus.setText("Authenticated!");
            // open main playlist window...


        } else {
            System.out.println("Authentication unsuccessful");
            lblAuthStatus.setText("Authentication error!");
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Login window initialised");
    }
}
