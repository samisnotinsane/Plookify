package account;

import account.PopupBoxes.PaymentPopUp;
import account.PopupBoxes.PopupLabelButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import player.FXMLDocumentController;

import java.net.URL;
import java.util.*;
/**
 * Created by tahnik on 14/03/2016.
 */
public class SettingsController implements Initializable{
    @FXML private Button addDevice;
    @FXML private TextField deviceName;
    @FXML private Button closeAccount;
    @FXML private TableColumn<Device, String> deviceTableName;
    @FXML private TableColumn<Device, String> deviceTableAdded;
    @FXML private TableColumn<Device, String> deviceTableType;
    @FXML private TableColumn<Device, String> deviceTableId;
    @FXML private TableView<Device> devicesTable;
    @FXML private Button replaceDevice;
    @FXML private TextField oldDeviceName;
    @FXML private TextField newDeviceName;
    @FXML private Button makePayment;
    @FXML private Button changeSubscription;
    @FXML private Label name;
    @FXML private Label accountType;
    @FXML private Label nextPaymentDue;
    @FXML private Button logout;
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private ChoiceBox<String> choiceBox1;

    private ObservableList<Device> devices;
    Button button = new Button();
    private ArrayList<Button> buttons = new ArrayList<>();
    Calendar now = Calendar.getInstance();
    Calendar nextPaymentDate = Calendar.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBox.setItems(FXCollections.observableArrayList("Console", "Desktop", "Mobile"));
        choiceBox1.setItems(FXCollections.observableArrayList("Console", "Desktop", "Mobile"));
        choiceBox.getSelectionModel().selectFirst();
        choiceBox1.getSelectionModel().selectFirst();
        changeTable();
        changeAccountTypeText();
        changeName();
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Stage)button.getScene().getWindow()).close();
            }
        });
        changeSubscription.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                makeMonthlyPayment();
            }
        });
        makePayment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                makeMonthlyPayment();
            }
        });
        addDevice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addDevice();
            }
        });
        deviceName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString().equals("ENTER")){
                    addDevice();
                }
            }
        });
        closeAccount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String singleQuery;
                singleQuery = "UPDATE USERSTATUS SET STATUS='" + 0 + "' WHERE USERID='" +
                        Utilities.getUserID(Authenticator.getUser().getEmail()) + "'";
                Utilities.executeSingleQuery(singleQuery);
                Authenticator.logout();
                FXMLDocumentController.stopTP();
                ((Stage) closeAccount.getScene().getWindow()).close();
                try {
                    new LoginStage();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        replaceDevice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                replaceDevice();
            }
        });
        oldDeviceName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString().equals("ENTER")){
                    replaceDevice();
                }
            }
        });
        newDeviceName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString().equals("ENTER")){
                    replaceDevice();
                }
            }
        });
    }
    public void changeTable(){
        devices = Utilities.getDevices();
        deviceTableName.setCellValueFactory(celldata -> celldata.getValue().getNameProperty());
        deviceTableAdded.setCellValueFactory(celldata -> celldata.getValue().getAddedAtString());
        deviceTableType.setCellValueFactory(celldata -> celldata.getValue().getTypeProperty());
        deviceTableId.setCellValueFactory(celldata -> celldata.getValue().idProperty());
        devicesTable.setItems(devices);
    }
    public void changeAccountTypeText(){
        if(checkPaymentDue()){
            accountType.setText("Premium");
        }else if(Authenticator.getUser().getSubscriptionType()) {
            accountType.setText("Premium");
            try {
                nextPaymentDate.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
                nextPaymentDue.setText("The next payment is due on " + nextPaymentDate.getTime() + "");
            } catch (Exception e) {
                //e.printStackTrace();
            }
        } else{
            accountType.setText("Free");
            nextPaymentDue.setText(" ");
        }
    }
    private boolean checkPaymentDue(){
        Calendar today = Calendar.getInstance();
        Calendar twoDayBefore = Calendar.getInstance();
        Calendar twoDayAfter = Calendar.getInstance();
        Calendar payment = Calendar.getInstance();
        Calendar expire = Calendar.getInstance();
        if(Authenticator.getUser().getSubscriptionType()){
            try {
                payment.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
                twoDayAfter.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
                twoDayBefore.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
                expire.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
                expire.add(Calendar.DATE, 2);
                twoDayBefore.add(Calendar.DATE, -2);
                twoDayAfter.add(Calendar.DATE, 2);
                System.out.println("TwoDayBefore: " + twoDayBefore.getTime());
                System.out.println("TwoDayAfter: " +twoDayAfter.getTime());
                if (today.after(twoDayBefore) && today.before(twoDayAfter)) {
                    nextPaymentDue.setText("Your payment is now due. Please make payment soon to prevent losing features");
                    return true;
                }
            }catch (Exception e){
                //
            }
        }
        return false;
    }
    private void makeMonthlyPayment(){
        if(Authenticator.getUser().getSubscriptionType()) {
            nextPaymentDate.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
            if (now.after(nextPaymentDate)) {
                try {
                    new PaymentStage();
                    changeAccountTypeText();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                buttons.clear();
                button.setText("Close");
                buttons.add(button);
                new PopupLabelButton((Stage) makePayment.getScene().getWindow(), "Sorry you can't make payment or change subscription before " +
                        nextPaymentDate.getTime(), buttons);
            }
        }else {
            try {
                new PaymentStage();
                changeAccountTypeText();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void changeName(){
        name.setText(Authenticator.getUser().getFirstName() + " " + Authenticator.getUser().getLastName());
    }
    public void replaceDevice(){
        oldDeviceName.setText(Utilities.escapeString(oldDeviceName.getText()));
        newDeviceName.setText(Utilities.escapeString(newDeviceName.getText()));
        Calendar calendar = Calendar.getInstance();
        Calendar addedAt = Calendar.getInstance();
        if(Utilities.checkDevice(oldDeviceName.getText())) {
            buttons.clear();
            addedAt.setTimeInMillis(Utilities.deviceAdded(oldDeviceName.getText()));
            addedAt.add(Calendar.MONTH, 1);
            button.setText("Close");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ((Stage) button.getScene().getWindow()).close();
                }
            });
            System.out.println("Account Manager");
            buttons.add(button);
            if(newDeviceName.getText().equals("")){
                buttons.clear();
                button.setText("Close");
                buttons.add(button);
                new PopupLabelButton((Stage) replaceDevice.getScene().getWindow(), "Please enter a valid" +
                        " new device name", buttons);
            }else if(calendar.before(addedAt)) {
                buttons.clear();
                button.setText("Close");
                buttons.add(button);
                new PopupLabelButton((Stage) replaceDevice.getScene().getWindow(), "You can't remove" +
                        " this device before " + addedAt.getTime(), buttons);
            }else{
                Utilities.removeDevice(oldDeviceName.getText());
                int type = 0;
                if(choiceBox1.getSelectionModel().getSelectedItem().equals("Desktop")){
                    type = 0;
                }else if(choiceBox1.getSelectionModel().getSelectedItem().equals("Console")){
                    type = 1;
                }else if(choiceBox1.getSelectionModel().getSelectedItem().equals("Mobile")){
                    type = 2;
                }
                Device device = new Device(newDeviceName.getText(), calendar.getTimeInMillis(), type, 1);
                device.addDevice();

            }
            changeTable();
        }else{
            buttons.clear();
            button.setText("Close");
            buttons.add(button);
            new PopupLabelButton((Stage) replaceDevice.getScene().getWindow(), "Please enter a valid Old Device Name", buttons);
        }
    }
    public void addDevice(){
        deviceName.setText(Utilities.escapeString(deviceName.getText()));
        Calendar calendar = Calendar.getInstance();
        System.out.println(Utilities.checkDevice(deviceName.getText()));
        if(Utilities.checkDevice(deviceName.getText())) {
            buttons.clear();
            button.setText("Close");
            buttons.add(button);
            new PopupLabelButton((Stage) addDevice.getScene().getWindow(), "Sorry the device already exists",
                    buttons);
        }else if(Utilities.deviceCount() >= 5){
            buttons.clear();
            button.setText("Close");
            buttons.add(button);
            new PopupLabelButton((Stage) addDevice.getScene().getWindow(), "Sorry you can only add 5 devices",
                    buttons);
        }else if(deviceName.getText().equals("")){
            buttons.clear();
            button.setText("Close");
            buttons.add(button);
            new PopupLabelButton((Stage) addDevice.getScene().getWindow(), "Please enter a valid name",
                    buttons);
        } else {
            int type = 0;
            if(choiceBox.getSelectionModel().getSelectedItem().equals("Desktop")){
                type = 0;
            }else if(choiceBox.getSelectionModel().getSelectedItem().equals("Console")){
                type = 1;
            }else if(choiceBox.getSelectionModel().getSelectedItem().equals("Mobile")){
                type = 2;
            }
            Device device = new Device(Utilities.escapeString(deviceName.getText()), calendar.getTimeInMillis(), type, 1);
            device.addDevice();
            changeTable();
        }
    }
}
