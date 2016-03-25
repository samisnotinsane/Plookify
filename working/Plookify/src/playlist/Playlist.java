package playlist;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Created by sameenislam on 28/01/2016.
 * Application logic for handling playlist objects.
 *
 */
public class Playlist extends Sequence {
    private int id; // playlist ID
    private String name;
    private int ownerId; // user ID
    private String type; // false: private, true: friends can access
    // Track player will need an ObservableList instead of an ArrayList
    private ArrayList<Sequence> trackListDeprecated; // contains trackIDs and trackList that are in this Playlist
    private ObservableList<Track> trackList = FXCollections.observableArrayList(); // contains songs in the playlist
    private int trackCount=0; // assigns a sequence no. to Tracks in Playlist

    // test constr

    public Playlist() {

    }

    public Playlist(String name) {
        this.name = name;
    }

    public Playlist(String name, int owner, String privacy) {
        //System.out.println("SOMETHING COOL!");
        this.name = name;
        this.ownerId = owner;
        this.type = privacy;
        this.id = SQL.returnLastPlaylistId();
        SQL.insertIntoPlaylist(name, type, ownerId); // commit header data to DB
    }

    // used by load() in PlaylistFactory
    public Playlist(int id, String name, String type, int ownerId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
        //System.out.println("[New playlist object loaded] "+ "id:[" +id+"] "+ " name: ["+name + "] " +
//                " privacy: [" +type + "] " + "owner id: [" +ownerId+"]");
        //System.out.println("New Playlist object creation complete!");

    }


    public ObservableList<Track> getTrackList() {
        // check DB for matching tracks
        loadContents();
        return trackList;
    }

    /**************************************************************
     * Returns the Tracks in this object by loading from DB first *
     **************************************************************/
    // load songs in this playlist object
    public void loadContents() {
        trackList.clear();
        //System.out.println("Calling Playlist.loadContents()");
//        loadContents(id); // playlist id
        ObservableList<Integer> rawTracks = FXCollections.observableArrayList();
        ObservableList<Integer> tracks = FXCollections.observableArrayList();
        // fetch raw trackList
        try {
            SQL util = new SQL();
            util.selectTracksMatchingPlaylist(this.id);

            ObservableList<Integer> playlistIdList = util.getPlaylistIdList();
            ObservableList<Integer> trackIdList = util.getTrackIdList();
            ObservableList<Integer> sequenceIdList = util.getSequenceIdList();
            ObservableList<Integer> plTrkList = util.getPlTrkList();


            // look up track
            for(int i=0; i<trackIdList.size(); i++) {
            Track trk = Track.loadTrack(trackIdList.get(i)); // takes track id
            trk.setSequenceNo(sequenceIdList.get(i));
            trackList.add(trk);
            }

//            rawTracks = SQL.selectTracksMatchingPlaylist(this.id);
//            //System.out.println("Printing contents of rawTracks:");
//            for (Integer in :
//                 rawTracks) {
//                //System.out.println(in);
//
//            }
//            if(rawTracks.get(0) == 0) {
//                //System.out.println("No tracks found in playlist no " + this.id+ " leaving trackList in Playlist empty");
//                return null;
//            }
        } catch (Exception e ) {
            //System.out.println("Failed to select matching playlist");
        }
        try {
            // parse track ids and get list of track
//            //System.out.println("Calling Track.parseTrackId(rawTracks)");
//            tracks = Track.parseTrackId(rawTracks);  // i.e. 9, 14
        } catch (Exception e) {
            //System.out.println("Failed to parse track id");
        }
        // look up these tracks in db and store each as a Track object
//        for(int i=0; i<tracks.size(); i++) {
//            //System.out.println("tracks.size()="+tracks.size());
//            //System.out.println("trackNo="+tracks.get(i));
//            //System.out.println("Calling Track.loadTrack("+tracks.get(i)+")");
//            trackList.add(Track.loadTrack(tracks.get(i))); // load a track object
//            //System.out.println(trackList.get(i).getName());
//        }

        //System.out.println("Returned to Playlist.loadContents()");
//        return null;
    }

//    public Playlist(ArrayList<Sequence> trackList) {
//        this.trackListDeprecated = trackList;
//    }

//


    public static void delete(Playlist playlist) {
        // supports only deleting the header for now
        if(SQL.deleteHeaderFromPlaylist(playlist.getId())) {
            //System.out.println("Playlist deleted");
        } else {
            //System.out.println("Playlist deletion failed");
        }

    }

    public String getName() {
        return name;
    }

    // fetches all rows from the PLAYLIST table
    public void getAllHeader() {
        ObservableList<String> list = SQL.selectAllFromPlaylist();

        // Do whatever with playlist headers...
        for(String header : list) {
            //System.out.println(header); // debug prints in this case
        }
    }

    public static ObservableList<String> getAllHeaderNames() {
        ObservableList<String> headers = SQL.selectAllPlaylistNames();
        return headers;
    }

    // inserts param values into Playlist table
    public boolean saveToDatabase(String name, String type, String addedBy) {
        return SQL.insertIntoPlaylist(name,type,Integer.parseInt(addedBy)); // returns true if successful
//        if(!successful)
//            //System.out.println("An error occurred when attempting to create new playlist '"+name+"'");
//        else {
//            //System.out.println("New playlist '"+name+"' created successfully");
//        }
    }

    public void showAll() {
        ObservableList<String> pList = SQL.selectAllFromPlaylist();
        try {
            if(!pList.get(0).equals("")) {
                for(String header : pList) {
                    //System.out.println(header);
                }
            }
        } catch(IndexOutOfBoundsException e) {
//            System.out.println("An error occurred while parsing playlist names\n"+
//                    "It's likely there are no playlists in database\n"+
//                    "Try adding a playlist and try again");
        }
    }

    // Assigns given Track to this Playlist
    public void addTrack(Track trk) {
        this.trackCount++; // sequence no = highest sequence no + 1

        int seq = this.trackCount; // get seq. no for this track
        String seqNo = Integer.toString(seq);

        int trkId = trk.getId();
        String plId = Integer.toString(this.id); // need to add this track against matching playlist

        boolean successful = SQL.insertIntoPlaylistTrack(trkId, plId, seqNo);

    }

    @Override
    public String toString() {
        return name;
    }

    // changes the name of the current object and updates it in the db
    public void rename(String newName) {
        boolean successful = SQL.changePlaylistName(this.getId(), newName);

    }

    // adds a sequence of trackList to a pre-existing playlist
    public void addSequence(ArrayList<Sequence> tracks) {
        // should Playlist be a subclass of Sequence?
            // add/remove/reorder methods in Sequence
        // Artist is also a sequence - inherit?
        // Both Artist and Playlist contain a sequence of trackList...
    }

    // adds a list of songs. Returns true if operation was successful.
    public boolean addSequence(ObservableList<Track> trackList) {
        boolean successVal = false;

        return successVal;
    }

    // getter
    public ArrayList<Sequence> getSequence() {
        return trackListDeprecated;
    }

    public String getType() { return type;}
    // setter: true = private, false = friend
    public void setType(boolean state) {
        if(state)
            this.type = "private";
        else
            this.type = "friend";
    }

    public int getId() {
        return id;
    }




    // an empty tracklist is an empty playlist
    public boolean isEmpty() {
        if(trackList != null && this.trackList.size() == 0)
            return true;
        else
            return false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setType(String type) {
        this.type = type;
    }

    // saves playlist header information into database tables.
    // Returns true upon successful save
    // CAUTION: This method DOES NOT save playlist contents!
    //public boolean saveHeader() {}

}
