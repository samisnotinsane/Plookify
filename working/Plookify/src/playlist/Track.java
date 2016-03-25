package playlist;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sameenislam on 02/02/2016.
 * Instantiates to a single Track object.
 */
public class Track {
    private Integer id; // track id
    private Integer plId; // playlist id
    private Integer sequenceNo=0; // determines ordering of track in a playlist
    private String name;
    private String artist;
    private Integer duration;

    private static ObservableList<Track> trackList = FXCollections.observableArrayList(); // will store all tracks
    private static ObservableList<ObservableList> data;
    private static ObservableList<ObservableList> seqData;

    public Track(Integer id, String name, String artist, Integer duration) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.duration = duration;

        //System.out.println("TRACK CONSTR");
    }


    public Track(Integer plId, Integer trkId, String name, String artist, Integer duration, Integer seqNo) {
        this.plId = plId;
        this.id = trkId;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        sequenceNo = seqNo;

        // Write to DB
        Connection c = null ;
        Statement s = null ;

        try {
            c = SQL.connect();
            //System.out.println("Track constructor opened database");

            c.createStatement();

            String sql = "INSERT INTO PLAYLISTTRACK(trackId, playlistId, trackSequenceNo) VALUES ("+
                    trkId+", "+plId+", "+seqNo+")";

            s.executeUpdate(sql);
            s.close();
            c.close();
            //System.out.println("Track constructor closed database");

        } catch (Exception e) {
            //System.out.println("Failed to create Track object. Database error.");
        }

    }

    // creates a new track in the playlist
    public void makeTrack() {

    }

    public void setSequenceNo(Integer n) {
        sequenceNo = n;
    }


    // load tracks from TRACKS table and return a record as object - for GUI
    public static Track loadTrack(int id) {
        // first get a track record from database
        ////System.out.println("Track.loadTrack() received id:"+id);
        ObservableList<String> trackInfoList = SQL.loadTrackById(id); // id, name, artist, duration
        // create a new Track object, supplying parameters from list above
        int tid = Integer.parseInt(trackInfoList.get(0));
        String tName = trackInfoList.get(1);
        String tArtist = trackInfoList.get(2);
        Integer tDuration = Integer.parseInt(trackInfoList.get(3)); // grabs a string
//        getPlaylistSequenceNo(playlist);
        Track track = new Track(tid, tName, tArtist, tDuration);
        //System.out.println("[New Track object loaded]: "+track.toString());
        return track;
    }

    // given a playlist, fetch seqNo of this track in that playlist
    public void getPlaylistSequenceNo(Playlist playlist) {
        //System.out.println("Calling getPlaylistSequenceNo");
        int seqNo = 0;
        int plId = playlist.getId();
        int trkId = this.getId();
        Connection c ;
        data = FXCollections.observableArrayList();
        try{
            c = SQL.connect();
            //System.out.println("Track.getPlaylistSequenceNo opened database");
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT trackSequenceNo FROM PLAYLISTTRACK WHERE playlistId = " +
            plId + "AND trackId = " + trkId + ";";

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);
            ObservableList<String> row = FXCollections.observableArrayList();
            while(rs.next()){
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
//                //System.out.println("Row [1] added "+row );
                sequenceNo = Integer.parseInt(row.get(0));
//                sequenceNo = 200;
                //System.out.println("TRK SEQ NO "+sequenceNo);
            }
            c.close();
            //System.out.println("Track.getPlaylistSequenceNo closed database");
        }catch(Exception e){
            e.printStackTrace();
            //System.out.println("Error on Building Data");
        }
    }


    // extracts track id into an OL<Int>
    public static ObservableList<Integer> parseTrackId(ObservableList<Integer> rawTracks) {

        ObservableList<Integer> readyList = FXCollections.observableArrayList();
        ////System.out.println("Creating idList, nameList, privacyList, ownerList");
        //ObservableList<Integer> idList = FXCollections.observableArrayList();
//        ObservableList<String> trackIdList = FXCollections.observableArrayList();
//        ObservableList<String> playlistIdList = FXCollections.observableArrayList();
//        ObservableList<Integer> sequenceNumberList = FXCollections.observableArrayList();
        //System.out.println("Initialising tracker to 0");
        int tracker = 0;
        //System.out.println("Showing rawTracks...");
        for(Integer num : rawTracks) {
            //System.out.println(num);
        }
        for(int i=0; i<rawTracks.size(); i++) {
            //System.out.println("i="+i);
            if(tracker == 1) {
                //System.out.println("tracker==trackID, rawTracks.get("+i+")= "+rawTracks.get(i));
                readyList.add(rawTracks.get(i));
                //System.out.println("adding to readyList track id="+rawTracks.get(i));
            }
            if (tracker == 4) {
                //System.out.println("tracker == 4");
                //System.out.println("Setting tracker to 0 again");
                tracker = 0;
            }
            tracker++;
            // ignore other raw values
        }
        //System.out.println("Printing contents of readylist");
        for (Integer num:
                readyList) {
            //System.out.println(num);
        }
        //System.out.println("Returning from Track.parseTrackId() to Playlist.loadContents()");
        return (ObservableList<Integer>) readyList;
    }


    public int getId() {
        return this.id;
    }

    public int getSequenceNo() {return this.sequenceNo;}

    public String getName() {
        return this.name;
    }

    public String getArtist() {
        return this.artist;
    }

    public Integer getDuration() { return this.duration; }

    public static ObservableList<Track> getAllTracks() {return trackList;}


    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", sequenceNo=" + sequenceNo +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                '}';
    }

    public static void loadAll() {

    }

//    public static void main(String[] args) {
//        Track.buildData();
//        generateObjects(); // finally generate Track objects from the data
//        //System.out.println("Printing Track objects from main...");
//        for(int i=0; i<trackList.size(); i++) {
//            //System.out.println(trackList.get(i).toString());
//        }
//    }

    //CONNECTION DATABASE
    public static void buildData(){
        Connection c ;
        data = FXCollections.observableArrayList();
        try{
            c = SQL.connect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT ID, NAME, ARTIST, DURATION FROM TRACKS";
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
//            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
//                //We are using non property style for making dynamic table
//                final int j = i;
//                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
//                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
//                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
//                        return new SimpleStringProperty(param.getValue().get(j).toString());
//                    }
//                });
//
//                tableview.getColumns().addAll(col);
//                //System.out.println("Column ["+i+"] ");
//            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                data.add(row);

            }
            c.close();
            //FINALLY ADDED TO TableView
//            tableview.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            //System.out.println("Error on Building Data");
        }

    }

    // will take rows of track data and turn them into track objects
    // which are then added to trackList<Track>
    public static void generateObjects() {

        for(int i=0; i<data.size(); i++) {
            //System.out.println("Row " + i + " " + data.get(i));
            ObservableList subgroup = data.get(i);

            Integer id = 0;
            String name = "";
            String artist = "";
            Integer duration = 0;

            for(int j=0; j<subgroup.size(); j++) {
                if(j==0) {
                    id = Integer.parseInt((String) subgroup.get(j));
                    //System.out.println("id=" + id);
                }
                if(j==1) {
                    name = (String) subgroup.get(j);
                    //System.out.println("name=" + name);
                }
                if(j==2) {
                    artist = (String) subgroup.get(j);
                    //System.out.println("artist=" + artist);
                }
                if(j==3) {
                    duration = Integer.parseInt((String) subgroup.get(j));
                    //System.out.println("duration=" + duration);
                }
            }

            Track track = new Track(id, name, artist, duration);
            //System.out.println("track.toString(): "+ track.toString());
            trackList.add(track);
            //System.out.println("Completed generating Track objects");
        }
    }



    // -----------------deprecated---------------------
    // creates a new record in the TRACK database table
    public void create(String name, String artistId, String duration, String genreId, String path) {
        // TODO: verify input artistId, duration, genreId can be casted to int
        boolean successful = SQL.insertIntoTrack(name, artistId, duration, genreId, path);
//        if(!successful)
//            //System.out.println("An error occurred when attempting to create new Track '"+name+"'");
//        else {
//            //System.out.println("New track '"+name+"' created successfully");
//        }
    }

    // gets all tracks from TRACK database table and print them in terminal
    public void showAll() {
        ArrayList<String> trkList = (ArrayList<String>) SQL.selectAllTrack();
//        ResultSet rs = SQL.selectAllTrack();
        try {
            if(trkList.get(0).equals(""))

                for(String metadata : trkList) {
                    //System.out.println(metadata);
                }
        } catch(IndexOutOfBoundsException e) {
            //System.out.println("An error occurred while parsing track information\n"+
//                    "It's likely there are no tracks in database\n"+
//                    "Try adding a track and try again");
        }
    }

    // ------------------------------------------------


}
