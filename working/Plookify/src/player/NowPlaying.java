package player;

import account.Authenticator;
import account.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kristian André Aspevik on 30/01/16.
 */
public class NowPlaying {

    public static NowPlaying nowPlaying = new NowPlaying();

    public static NowPlaying getInstance(){
        return nowPlaying;
    }


    private ObservableList<Tracks> queueList;
    private ObservableList<Tracks> originalList;
    private int startIndex;
    private String sqlStmt = "";
    private ResultSet rs = null;
    private int counter = 0;
    private boolean repeating = false;
    private boolean shuffle = false;

    @FXML private TableView<Tracks> nowPlayingTable;
    @FXML private TableColumn nowPlayingName;
    @FXML private TableColumn nowPlayingDuration;

    private NowPlaying() {
    }

    public Tracks getCurrentSong(){
        return queueList.get(0);
    }

    public void setRepeating(boolean repeating){
        this.repeating = repeating;
    }

    public void setShuffle(boolean shuffle){
        this.shuffle = shuffle;
    }

    public int getCounter(){
        return counter;
    }

    public void nextSong(){
        if(!repeating) {
            counter++;
            try {
                setQueueAgain();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public String playSong(){
        if(!queueList.isEmpty()) {
            return originalList.get(counter).getPath();
        }
        return null;
    }

    public void previousSong() {
        if(counter != 0) {
            counter--;
        }
        try {
            setQueueAgain();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void loadQueue(){
        counter = 0;
        queueList  = FXCollections.observableArrayList();
        originalList = FXCollections.observableArrayList();
        DBtest dbTest = new DBtest();
        int userID = Utilities.getUserID(Authenticator.getUser().getEmail()); //Utilities.getUserID(Authenticator.getUser().getEmail());
        String stmt = "SELECT TRACKS FROM NOWPLAYINGQUEUE WHERE USERID = " + userID + "";
        rs = dbTest.select(stmt);
        String str = null;
        try {
            str = rs.getString("TRACKS");
        } catch (SQLException e) {
            try {
                dbTest.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        List<String> storedList = Arrays.asList(str.split(","));
        try {
            dbTest.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbTest = new DBtest();
        for (String aStoredList : storedList) {
            Tracks e;
            int id = Integer.parseInt(aStoredList);

            stmt = "SELECT * FROM TRACKS WHERE ID = " + id + "";
            rs = dbTest.select(stmt);
            try {
                while (rs.next()) {
                    e = new Tracks(rs.getInt("ID"), rs.getString("NAME"), rs.getString("ARTIST"), rs.getInt("DURATION"), rs.getString("GENRE"), rs.getString("PATH"), rs.getString("ALBUM"), rs.getString("ALBUMPATH"));
                    originalList.add(e);
                    queueList.add(e);
                }
            } catch (SQLException a) {
                a.printStackTrace();
            }
        }
        try {
            dbTest.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        populateNowPlayingInit();

    }
    public void storeQueue() throws SQLException {
        String tracks = "";
        for (Tracks aQueueList : queueList) {
            tracks += aQueueList.getID() + ",";
        }
        int userID = Utilities.getUserID(Authenticator.getUser().getEmail()); //Utilities.getUserID(Authenticator.getUser().getEmail());
        DBtest dbTest = new DBtest();
        String stmt = "UPDATE NOWPLAYINGQUEUE SET TRACKS = '" + tracks + "' where USERID = " + userID + "";
        boolean test = dbTest.update(stmt);
        dbTest.close();
        if(!test){
            dbTest = new DBtest();
            stmt = "INSERT INTO NOWPLAYINGQUEUE (USERID, TRACKS) VALUES (" + userID + ", '" + tracks + "')";
            dbTest.insert(stmt);
            dbTest.close();
        }
    }

    public void setQueue(ObservableList<Tracks> queue, int startIndex) {
        this.originalList   = queue;
        this.startIndex = startIndex;
        counter = startIndex;
        queueList = FXCollections.observableArrayList(originalList);
        sort();
        if(shuffle){
            shuffleList();
        }
        try {
            storeQueue();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setQueueAgain() throws SQLException {
        queueList = FXCollections.observableArrayList(originalList);
        sort2();
        if(shuffle){
            shuffleList();
        }
        storeQueue();
    }


    public ObservableList<Tracks> getQueue() {
        return queueList;
    }

    public ObservableList<Tracks> getQueueList() {
        return queueList;
    }

    public boolean isEmpty(){
        boolean flag;
        flag = queueList.size() == 0;
        return flag;
    }

    public void sort() {
        queueList.remove(0,startIndex);
    }

    public void sort2() {
        queueList.remove(0,counter);
    }



    public void addPlaylist(ObservableList<Tracks> playlist){
        originalList.addAll(playlist);
        queueList.addAll(playlist);
        populateNowPlaying(queueList);
    }

    public void addPlaylist2(List<Integer> playlist) throws SQLException {
        DBtest a = new DBtest();
            for (Integer aPlaylist : playlist) {
            sqlStmt = "SELECT * FROM TRACKS where ID like '%" + aPlaylist + "%'";

            rs = a.select(sqlStmt);
            originalList.add(new Tracks(rs.getInt("ID"), rs.getString("NAME"), rs.getString("ARTIST"), rs.getInt("DURATION"), rs.getString("GENRE"), rs.getString("PATH"), rs.getString("ALBUM"), rs.getString("ALBUMPATH")));
            queueList.add(new Tracks(rs.getInt("ID"), rs.getString("NAME"), rs.getString("ARTIST"), rs.getInt("DURATION"), rs.getString("GENRE"), rs.getString("PATH"), rs.getString("ALBUM"), rs.getString("ALBUMPATH")));
        }
        rs.close();
        a.close();
        populateNowPlaying(getQueueList());
    }


    public void sendComponents(TableView<Tracks> nowPlayingTable, TableColumn nowPlayingName, TableColumn nowPlayingDuration){
        this.nowPlayingTable = nowPlayingTable;
        this.nowPlayingName = nowPlayingName;
        this.nowPlayingDuration = nowPlayingDuration;
        nowPlayingTable.setPlaceholder(new Label("No songs in the queue"));
    }

    public void populateNowPlaying(ObservableList<Tracks> data){
        nowPlayingTable.setEditable(true);
        nowPlayingName.setCellValueFactory(new PropertyValueFactory<Tracks,String>("name"));
        nowPlayingDuration.setCellValueFactory(new PropertyValueFactory<Tracks,String>("duration"));
        nowPlayingTable.setItems(data);
    }

    public void populateNowPlayingInit(){
        nowPlayingTable.setEditable(true);
        nowPlayingName.setCellValueFactory(new PropertyValueFactory<Tracks,String>("name"));
        nowPlayingDuration.setCellValueFactory(new PropertyValueFactory<Tracks,String>("duration"));
        nowPlayingTable.setItems(queueList);
    }



    public void removeTrackID(int pos){
        if(pos != 0) {
            queueList.remove(pos);
            originalList.remove(counter + pos);
        }
        try {
            storeQueue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTrack(int trackID) throws SQLException {
        DBtest a = new DBtest();
        sqlStmt = "SELECT * " +
                "FROM TRACKS where ID like '%" + trackID + "%'";

        rs = a.select(sqlStmt);

        queueList.add(new Tracks(rs.getInt("ID"),rs.getString("NAME"),rs.getString("ARTIST"),rs.getInt("DURATION"), rs.getString("GENRE"), rs.getString("PATH"), rs.getString("ALBUM"), rs.getString("ALBUMPATH")));
        originalList.add(new Tracks(rs.getInt("ID"),rs.getString("NAME"),rs.getString("ARTIST"),rs.getInt("DURATION"), rs.getString("GENRE"), rs.getString("PATH"), rs.getString("ALBUM"), rs.getString("ALBUMPATH")));
        rs.close();
        a.close();
        try {
            storeQueue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTrackAsNext(Tracks track){
        queueList.add(1, track);
        originalList.add(counter+1, track);
        populateNowPlayingInit();
        try {
            storeQueue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void shuffleList(){
        List<Tracks> subList = queueList.subList(1,queueList.size());
        Collections.shuffle(subList);
        int a = counter;
        for(int i = 0; i<queueList.size()-1; i++) {
            originalList.set(++a, subList.get(i));
        }
        try {
            storeQueue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearQueue(){
        originalList.remove(counter+1, originalList.size());
        queueList.remove(1, queueList.size());
        populateNowPlaying(queueList);
        try {
            storeQueue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}