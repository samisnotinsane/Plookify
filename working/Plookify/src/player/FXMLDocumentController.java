/************************
 AUTHOR: Kristian Andre Aspevik
 This is the controller for the FXMLDocument.fxml file. It handles all the events in the GUI, such as playing a track, creating
 objects etc.
 ************************/
package player;

import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;

import static javafx.scene.input.KeyCode.ENTER;


/**
 *
 * @author Kristian
 */

public class FXMLDocumentController extends Application implements Initializable {

    private String sqlStmt = "";
    private ResultSet rs = null;
    private static MediaPlayer mediaPlayer = null;
    private Media media = null;
    private Scene scene;
    private DBtest a = null;
    private boolean doubleClick = false;


    private double dur;


    private Thread th;
    private boolean flag = false;
    private boolean dragFlag = false;
    private boolean allowedToClick = false;

    static private Stage primaryStage;

    private final ContextMenu cm = new ContextMenu();

    private final ContextMenu npMenu = new ContextMenu();
    private final MenuItem npItem1 = new MenuItem("Remove from Queue");
    private String minutes;
    private String seconds;


    private int currentSongID;
    private ObservableList<Tracks> currentTracks = null;
    private final NowPlaying queueList = NowPlaying.getInstance();

    @FXML private TextField textField;
    @FXML private Pane mainPane;

    @FXML private TableView<Tracks> trackTable;
    @FXML private TableColumn name;
    @FXML private TableColumn artist;
    @FXML private TableColumn duration;
    @FXML private TableColumn genre;

    @FXML private Tracks row = null;

    @FXML private Pane albumArtwork;

    @FXML private Button playBtn;
    @FXML private Button nextBtn;
    @FXML private Button prevBtn;
    @FXML private Button deleteAll;
    @FXML private ToggleButton repeat;
    @FXML private ToggleButton shuffle;


    @FXML private Label durLabel;
    @FXML private Label elapsedLabel;
    @FXML private Label trackLabel;
    @FXML private Label artistLabel;

    @FXML private TableView<Tracks> nowPlayingTable;
    @FXML private TableColumn nowPlayingName;
    @FXML private TableColumn nowPlayingDuration;

    @FXML private Slider volSlider;
    @FXML private ProgressBar pb;
    @FXML private Slider durationSlider = new Slider(0, 100, 0);

    /************************
     Creates/closes a connection to the DB using the class DBtest.
     ************************/
    public void connectToDB(){
        a = new DBtest();
    }

    public void closeDB() throws SQLException {
        a.close();
        a = null;
    }


    /************************
     Eventhandler for the Search textfield. Send SQL Query to Database with the contents of the textField whenever
     you hit the "ENTER" key on the keyboard.
     ************************/
    @FXML
    public void handleEnterPressed(KeyEvent event) throws SQLException {
        if(event.getCode() == ENTER){
            search();
        }
    }

    public void search() throws SQLException {
        String values = textField.getText();
        values = account.Utilities.escapeString(values);
        connectToDB(); //Create connection to the DB.
        sqlStmt = "SELECT * FROM TRACKS where ARTIST like '%" + values + "%' or GENRE like '%" + values + "%' or NAME like '%" + values + "%' order by ARTIST asc, NAME;";
        rs = a.select(sqlStmt);
        ObservableList<Tracks> data = makeList(rs);
        populateTable(data);

        rs.close();
        a.close2();
    }



    public void startCounting(){

        mediaPlayer.setOnPlaying(()-> {
            try{
                if(th.isAlive()){
                    return;
                }
            }catch(Exception e){
            }

            th = new Thread() {
                @Override
                public void run() {
                    Duration a;
                    while (flag && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                        try {
                            while (dragFlag) {
                                try {
                                    Thread.sleep(50);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            a = mediaPlayer.getCurrentTime();
                            Duration comDur = mediaPlayer.getMedia().getDuration();
                            NumberFormat formatter = new DecimalFormat("00");

                            double currentTime = a.toSeconds();
                            int currentTimeSec = (int)a.toSeconds() % 60;
                            int currentTimeMin = (int)a.toSeconds() / 60;
                            String sec = formatter.format(currentTimeSec);
                            Platform.runLater(() -> elapsedLabel.setText(currentTimeMin + ":" + sec));
                            Platform.runLater(() -> durationSlider.setValue(currentTime / comDur.toSeconds() * 100));  //Testing the setValue() method
                        }catch(NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            th.start();
        });
    }

    public void setRepeat(){
        if(repeat.isSelected()){
            queueList.setRepeating(true);
        }
        else if(!repeat.isSelected()){
            queueList.setRepeating(false);
        }
    }


    public void stopCounting(){
        dragFlag = true;
    }

    public void startAgain(){
        double sliderDur = durationSlider.getValue();
        double finalDur = 0;
        if(mediaPlayer != null) {
            finalDur = sliderDur / 100 * mediaPlayer.getMedia().getDuration().toSeconds();
            Duration aDur = new Duration(finalDur*1000);
            dragFlag = false;
            playSong(aDur);
        }
        else{
            durationSlider.setValue(0);
        }

    }


    /************************
     Creates the context menu (i.e. the menu when you right click in the main tableview) and handles the events
     depending on which menuitem you click on.
     ************************/
    public void createMenu(){

        MenuItem cmItem1 = new MenuItem("Play");
        MenuItem cmItem2 = new MenuItem("Add to Queue");
        MenuItem cmItem4 = new MenuItem("Play Next");
        MenuItem cmItem3 = new MenuItem("Start Track At:");

        cmItem1.setOnAction(event -> {
            if(mediaPlayer == null) {
                currentSongID = trackTable.getSelectionModel().getFocusedIndex();
                currentTracks = FXCollections.observableArrayList(trackTable.getItems());
                queueList.setQueue(currentTracks, currentSongID);
                playSong();
            }
            if(mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING )){
                if (currentSongID == trackTable.getSelectionModel().getSelectedItem().getID()){
                    pauseSong();
                }
                else {
                    currentSongID = trackTable.getSelectionModel().getFocusedIndex();
                    currentTracks = FXCollections.observableArrayList(trackTable.getItems());
                    queueList.setQueue(currentTracks, currentSongID);
                    playSong();
                }
            }
            else if(mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)){
                if (currentSongID == trackTable.getSelectionModel().getSelectedItem().getID()){
                    continueSong();
                }
                else {
                    playSong();
                }
            }
        });

        cmItem2.setOnAction(event -> {
            try {
                queueList.addTrack(trackTable.getSelectionModel().getSelectedItem().getID());
                populateNowPlaying(queueList.getQueue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        cmItem3.setOnAction(event -> {
            currentSongID = trackTable.getSelectionModel().getFocusedIndex();
            currentTracks = FXCollections.observableArrayList(trackTable.getItems());
            queueList.setQueue(currentTracks, currentSongID);
            showDialog();

        });

        cmItem4.setOnAction(event -> {
            Tracks track = trackTable.getSelectionModel().getSelectedItem();
            queueList.addTrackAsNext(track);
        });
        cm.getItems().clear();
        cm.getItems().add(cmItem1);
        cm.getItems().add(cmItem2);
        cm.getItems().add(cmItem4);
        cm.getItems().add(cmItem3);

    }


    /************************
     Method to play a song from a specific point.
     Overloaded method.
     ************************/
    public void playSong(Duration start){
        try {
            if(queueList.getQueueList() == null) {
                currentTracks = FXCollections.observableArrayList(trackTable.getItems());
            }

            currentTracks = queueList.getQueue();
            String path;
            populateNowPlaying(queueList.getQueue());

                path = queueList.playSong();


            final URL resource = getClass().getResource(path);
            if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                mediaPlayer.stop();
            }

            Tracks track = queueList.getCurrentSong();
            media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setStartTime(start);
            mediaPlayer.play();
            mediaPlayer.setVolume(volSlider.getValue() / 100);
            updateSongStats();
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/PauseIcon.png');" +
                    "    -fx-background-size: contain;" +
                    "    -fx-background-repeat: no-repeat;");
            //metaData = media.getMetadata();
            doubleClick = false;
            String artSrc = "/player/" + track.getAlbumPath() + "/" + track.getAlbum() + ".jpg";
            albumArtwork.setStyle("-fx-background-image: url('" + artSrc + "');" +
                    "    -fx-background-size: cover;" + "-fx-background-repeat: no-repeat");
            //metaData = media.getMetadata();
            doubleClick = false;
            setDuration();
            fetchNextSong();
            startCounting();
        }catch(Exception e){

        }

    }

    /************************
     Creates the context menu (i.e. the menu when you right click in the secondary tableview which is Now Playing queue)
     and handles the events depending on which menuitem you click on.
     ************************/
    public void createMenu2(){
        npItem1.setOnAction(event -> {
                int removeT = nowPlayingTable.getSelectionModel().getSelectedIndex();
                queueList.removeTrackID(removeT);
                populateNowPlaying(queueList.getQueue());
        });
        npMenu.getItems().clear();
        npMenu.getItems().add(npItem1);
    }

    /************************
     Displays the context menu for Now Playing queue or hides if it if is already displaying.
     ************************/
    public void nowPlayingListener(){
        nowPlayingTable.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown()){
                npMenu.hide();
            }
            if(event.isSecondaryButtonDown()){
                npMenu.hide();
                createMenu2();
                if(nowPlayingName.getCellData(0) != null) {
                    npMenu.show(nowPlayingTable, event.getScreenX(), event.getScreenY());
                }
            }
            mainPane.requestFocus();
        });
    }

    /************************
     Handles the events for clicking on the main tableview (i.e. trackTable). If you click one, hide the context menu.
     If you click twice, play the song you doubleclicked on.
     If you rightclick, display the context menu.
     ************************/
    @FXML
    public void clickListener(){
        trackTable.setOnMousePressed(event -> {
            if (!trackTable.getItems().isEmpty()) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    cm.hide();
                } else if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        currentSongID = trackTable.getSelectionModel().getFocusedIndex();
                        currentTracks = FXCollections.observableArrayList(trackTable.getItems());
                        doubleClick = true;
                        queueList.setQueue(currentTracks, currentSongID);
                        playSong();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                } else if (event.isSecondaryButtonDown()) {
                    createMenu();
                    if (name.getCellData(0) != null) {
                        cm.show(trackTable, event.getScreenX(), event.getScreenY());
                    }
                }
                mainPane.requestFocus();
            }
        });
    }

    /************************
     Displays the context menu.
     ************************/
    public void showMenu(){
        trackTable.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    if (e.getButton() == MouseButton.SECONDARY)
                        cm.show(trackTable, e.getScreenX(), e.getScreenY());
                });
    }

    /************************
     A global listener to listen for the user to press "SPACE". Clicking "SPACE" will play/pause the music, unless the
     textfield is focused, and if so, do nothing.
     ************************/
    public void mainPaneKey(KeyEvent event){
        if(event.getCode() == KeyCode.SPACE){
            if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
                pauseSong();
            }
            else if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)){
                continueSong();
            }
            else if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.STOPPED)){
                playSong();
            }
            if(mediaPlayer == null){
                playSong();
            }
        }
    }

    public static void stopTP(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /************************
     Eventhandler for the Play Button.
     ************************/
    @FXML
    public void playListener(MouseEvent event){
        if(event.isPrimaryButtonDown()){
            if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
                pauseSong();
            }
            else if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)){
                continueSong();
            }
            else if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.STOPPED)){
                playSong();
            }
            if(mediaPlayer == null){
                playSong();
            }

        }
        //mainPane.requestFocus();
    }

    /************************
     Fetches the path of the song to be played. Creates a MediaPlayer object and then plays the mp3.
     ************************/
    public void playSong(){
        try {
            if(queueList.getQueueList() == null) {
                currentTracks = FXCollections.observableArrayList(trackTable.getItems());
            }
            String path;
            populateNowPlaying(queueList.getQueue());

            path = queueList.playSong();

            final URL resource = getClass().getResource(path);
            if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                mediaPlayer.stop();
            }
            Tracks track = queueList.getCurrentSong();
            media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            mediaPlayer.setVolume(volSlider.getValue() / 100);
            updateSongStats();
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/PauseIcon.png');" +
                    "    -fx-background-size: contain;" +
                    "    -fx-background-repeat: no-repeat;");
            //metaData = media.getMetadata();
            doubleClick = false;
            String artSrc = "/player/" + track.getAlbumPath() + "/" + track.getAlbum() + ".jpg";
            albumArtwork.setStyle("-fx-background-image: url('" + artSrc + "');" +
                    "    -fx-background-size: cover;" + "-fx-background-repeat: no-repeat");
            /*BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(new File(track.getAlbumPath() + "/" + track.getAlbum() + ".jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            albumArtwork.setImage(image);*/

            setDuration();
            fetchNextSong();
            startCounting();
        }catch(Exception e){

        }
    }

    /************************
     Decides which song to be played next.
     ************************/
    public void fetchNextSong(){
        //currentTracks = queueList.getQueue();
        mediaPlayer.setOnEndOfMedia(() -> {
            queueList.nextSong();
            queueList.removeTrackID(0);
            populateNowPlaying(queueList.getQueueList());
            playSong();
        });
    }

    /************************
     Gets the total duration of the current track and displays it in a label on the GUI.
     ************************/
    public void setDuration(){
        mediaPlayer.setOnReady(() -> {
            allowedToClick = true;
            NumberFormat formatter = new DecimalFormat("00");
            dur = mediaPlayer.getMedia().getDuration().toSeconds();
            int min = (int)dur/60;
            int sec = (int)dur % 60;
            String s = formatter.format(sec);
            durLabel.setText(min + ":" + s);
            flag = true;
        });
    }

    /************************
     Eventhandler for the Next Button. When the button is clicked, you play the next song in the Now Playing queue.
     ************************/
    public void clickNext(){

        if(mediaPlayer != null && allowedToClick && queueList.getQueueList().size() != 1) {
            queueList.nextSong();
            allowedToClick = false;
            populateNowPlaying(queueList.getQueueList());
            playSong();

        }
        mainPane.requestFocus();
    }

    public void previousSong(){
        if(mediaPlayer != null && allowedToClick) {
            queueList.previousSong();
            allowedToClick = false;
            populateNowPlaying(queueList.getQueueList());
            playSong();
        }
        mainPane.requestFocus();

    }

    public void continueSong(){
        mediaPlayer.play();
        playBtn.setStyle("-fx-background-image: url('/player/resources/icons/PauseIcon.png');" +
                "    -fx-background-size: contain;" +
                "    -fx-background-repeat: no-repeat;");
    }

    public void updateSongStats(){
        artistLabel.setText(queueList.getQueue().get(0).getArtistName());
        trackLabel.setText(queueList.getQueue().get(0).getName());
    }


    /*********************
     Adds a listener to check if they change the volume slider. Adjusts the volume accordingly.
     *********************/
    public void volumeDetect(){
        volSlider.valueProperty().addListener(observable -> {
            if(mediaPlayer != null) {
                double sliderVol = volSlider.getValue() / 100;
                pb.setProgress(sliderVol);
                mediaPlayer.setVolume(sliderVol);
            }
        });

    }

    public void muteVol(){
        volSlider.setValue(0);
        pb.setProgress(0);
        if(mediaPlayer != null) {
            mediaPlayer.setVolume(0);
        }
    }

    public void topVol(){
        volSlider.setValue(100);
        pb.setProgress(100);
        if(mediaPlayer != null) {
            mediaPlayer.setVolume(100);
        }
    }


    public void addAll(){
        ObservableList<Tracks> trackResults = trackTable.getItems();
        queueList.addPlaylist(trackResults);
        try {
            queueList.storeQueue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /************************
     Pauses the song and changes the text of the button.
     ************************/
    public void pauseSong(){
        mediaPlayer.pause();
        playBtn.setStyle("-fx-background-image: url('/player/resources/icons/playIcon.png');" +
                "    -fx-background-size: contain;" +
                "    -fx-background-repeat: no-repeat;");
    }

    /************************
     Takes the track objects passed to the method and populates the TableView component.
     ************************/
    private void populateTable(ObservableList<Tracks> data) {
        trackTable.setEditable(true);
        name.setCellValueFactory(new PropertyValueFactory<Tracks,String>("name"));
        artist.setCellValueFactory(new PropertyValueFactory<Tracks,String>("artistName"));
        duration.setCellValueFactory(new PropertyValueFactory<Tracks,String>("duration"));
        genre.setCellValueFactory(new PropertyValueFactory<Tracks,String>("genre"));
        trackTable.setItems(data);
    }

    /************************
     Takes the track objects passed to the method and calls the correct method in NowPlaying.java which in turn
     populates the TableView component for the nowPlaying queue.
     ************************/
    public void populateNowPlaying(ObservableList<Tracks> data){
        queueList.populateNowPlaying(data);
    }

    /************************
     Takes the result set and creates an observableList from the data.
     ************************/
    public ObservableList<Tracks> makeList(ResultSet rs) throws SQLException {
        ObservableList<Tracks> data = FXCollections.observableArrayList();
        Tracks e;

        while(rs.next()) {
            e = new Tracks(rs.getInt("ID"),rs.getString("NAME"),rs.getString("ARTIST"),rs.getInt("DURATION"), rs.getString("GENRE"), rs.getString("PATH"), rs.getString("ALBUM"), rs.getString("ALBUMPATH"));
            data.add(e);
        }
        return data;

    }

    /************************
     Sets initial values of certain components.
     ************************/
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            search();
            trackTable.setPlaceholder(new Label("No results found. Try again"));
            volSlider.setValue(100);
            pb.setProgress(100);
            queueList.sendComponents(nowPlayingTable, nowPlayingName, nowPlayingDuration);
            queueList.loadQueue();
            if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/PauseIcon.png');" +
                    "    -fx-background-size: contain;" +
                    "    -fx-background-repeat: no-repeat;");
            else{
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/playIcon.png');" +
                    "    -fx-background-size: contain;" +
                    "    -fx-background-repeat: no-repeat;");
            }

        }catch(Exception e){
        }
    }

    /************************
     Loads up the FXML document and sets the stage.
     ************************/
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    /************************
     Creates and displays the dialog window for starting a song at a specific point
     ************************/
    public void showDialog(){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Dialog");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        dialog.initStyle(StageStyle.UNDECORATED);

        // Set the button types.
        ButtonType buttonOK = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonOK, buttonCANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField minutes2 = new TextField();
        minutes2.setPromptText("Minutes");
        TextField seconds2 = new TextField();
        seconds2.setPromptText("Seconds");
        Label wrong = new Label("Please input a number");

        grid.add(wrong,2,0);
        grid.add(new Label("Minutes:"), 1, 1);
        grid.add(minutes2, 2, 1);
        grid.add(new Label("Seconds:"), 1, 2);
        grid.add(seconds2, 2, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(param -> {
            if(param == buttonOK){
                try {
                    if (minutes2.getText().equals("")) {
                        minutes = "0";
                    } else {
                        minutes = minutes2.getText();
                    }
                    if (seconds2.getText().equals("")) {
                        seconds = "0";
                    } else {
                        seconds = seconds2.getText();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                return new Pair<>(minutes, seconds);
            }
            else{
                return null;
            }
        });
        Optional<Pair<String,String>> result = dialog.showAndWait();
        if(result.isPresent()) {
            handleOKButton();
        }
    }

    /************************
     Handles the event when you click OK in the dialog box
     ************************/
    public void handleOKButton(){
        int minutes3;
        int seconds3;
        try {
            minutes3 = Integer.parseInt(minutes);
            seconds3 = Integer.parseInt(seconds);
            int totalDur = (minutes3 * 60) + seconds3;
            if(totalDur >= trackTable.getSelectionModel().getSelectedItem().getDurationSecs()){
                showDialogAgain();
                return;
            }
        }
        catch(Exception e){
            showDialogAgain();
            return;
        }
        int milliseconds = (minutes3 * 60 * 1000) + (seconds3 * 1000);
        Duration dur1 = new Duration(milliseconds);
        doubleClick = true;
        playSong(dur1);
    }

    public void showDialogAgain(){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Dialog");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        dialog.initStyle(StageStyle.UNDECORATED);

        // Set the button types.
        ButtonType buttonOK = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonOK, buttonCANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField minutes2 = new TextField();
        minutes2.setPromptText("Minutes");
        TextField seconds2 = new TextField();
        seconds2.setPromptText("Seconds");
        Label wrong = new Label("Please input a valid number");
        wrong.setStyle("-fx-text-fill: red;");

        grid.add(wrong,2,0);
        grid.add(new Label("Minutes:"), 1, 1);
        grid.add(minutes2, 2, 1);
        grid.add(new Label("Seconds:"), 1, 2);
        grid.add(seconds2, 2, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(param -> {
            if(param == buttonOK){
                try {
                    if (minutes2.getText().equals("")) {
                        minutes = "0";
                    } else {
                        minutes = minutes2.getText();
                    }
                    if (seconds2.getText().equals("")) {
                        seconds = "0";
                    } else {
                        seconds = seconds2.getText();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                return new Pair<>(minutes, seconds);
            }
            else{
                return null;
            }
        });
        Optional<Pair<String,String>> result = dialog.showAndWait();
        if(result.isPresent()) {
            handleOKButton();
        }
    }

    public void clearQueue(){
        queueList.clearQueue();
    }

    public void setShuffle(){
        if(shuffle.isSelected()){
            queueList.setShuffle(true);
        }
        else{
            queueList.setShuffle(false);
        }
    }

    public void hoverPlay(){
        if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/PauseIcon2.png');" +
                    "    -fx-background-size: contain;" +
                    "    -fx-background-repeat: no-repeat;");
        }
        else if(mediaPlayer != null){
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/playIcon2.png');" +
                    "    -fx-background-size: contain;" +
                    "    -fx-background-repeat: no-repeat;");
        }
        else{
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/playIcon2.png');" +
                    "    -fx-background-size: contain;" +
                    "    -fx-background-repeat: no-repeat;");
        }
    }

    public void stopHoverPlay(){
        if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/PauseIcon.png');" +
                    "-fx-background-size: contain;" +
                    "-fx-background-repeat: no-repeat;");
        }
        else if(mediaPlayer != null){
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/playIcon.png');" +
                    "-fx-background-size: contain;" +
                    "-fx-background-repeat: no-repeat;");
        }
        else{
            playBtn.setStyle("-fx-background-image: url('/player/resources/icons/playIcon.png');" +
                    "-fx-background-size: contain;" +
                    "-fx-background-repeat: no-repeat;");
        }
    }
}