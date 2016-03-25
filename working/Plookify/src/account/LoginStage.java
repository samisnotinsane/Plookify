package account;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by tahnik on 15/03/2016.
 */
public class LoginStage extends Stage{
    GridPane Authenticator;
    Scene scene;

    public LoginStage() throws Exception{
        //System.out.println(getClass().getResource("login.fxml"));
        FXMLLoader loginfx = new FXMLLoader(getClass().getResource("login.fxml"));
        FXMLLoader registrationfx = new FXMLLoader(getClass().getResource("registration.fxml"));

        LoginController loginController = new LoginController();
        RegistrationController registrationController = new RegistrationController();

        loginfx.setController(loginController);
        registrationfx.setController(registrationController);

        GridPane login = (GridPane) loginfx.load();
        GridPane registration = (GridPane) registrationfx.load();

        Authenticator = new GridPane();

        //Authenticator.setStyle("-fx-background-image: url(resources/register.jpg);");

        Authenticator.getColumnConstraints().add(new ColumnConstraints(1024));
        Authenticator.getColumnConstraints().add(new ColumnConstraints(1024));

        Authenticator.add(login, 0, 0);
        Authenticator.add(registration, 1, 0);

        scene = new Scene(Authenticator, 1024, 768);

        this.setScene(scene);
        this.show();
    }
}
