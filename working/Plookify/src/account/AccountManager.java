/**
 * Created by tahnik on 28/01/2016.
 * This is the main class responsible for initiating everything in the account manager
 * This class opens the login stage that take the user through the authentication
 */

package account;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Calendar;

public class AccountManager extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        new LoginStage();
    }
    public static void main(String[] args){
        launch(args);
    }
}