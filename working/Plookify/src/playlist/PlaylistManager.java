package playlist;

import account.Authenticator;
import account.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import player.NowPlaying;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by sameenislam on 16/02/2016.
 * Manipulates a Playlist object.
 */
public class PlaylistManager {
    public Playlist playlist;
    private static ObservableList<ObservableList> playlistData;
    public PlaylistManager() {

    }

    public PlaylistManager(Playlist playlist) {
        this.playlist = playlist;
    }

//    public static Playlist getInstance() {
//        if(playlistMan==null) {
//            playlistMan = new PlaylistManager();
//        }
//        return playlistMan;
//    }

    // loads all playlist records from db and makes playlist objects out of them
    public static ObservableList<Playlist> loadAll() {
        // the final list of Playlist objects that is returned to the controller
        ////System.out.println("Creating readyList");
        ObservableList<Playlist> readyList = FXCollections.observableArrayList();


        ////System.out.println("Creating rawList");
        ObservableList<String> rawList = SQL.selectAllFromPlaylist(); // stores contents of rows from the PLAYLIST table

        // data from rawList is broken up into different lists according to columns
        ////System.out.println("Creating idList, nameList, privacyList, ownerList");
        ObservableList<Integer> idList = FXCollections.observableArrayList();
        ObservableList<String> nameList = FXCollections.observableArrayList();
        ObservableList<String> privacyList = FXCollections.observableArrayList();
        ObservableList<Integer> ownerList = FXCollections.observableArrayList();

        /*
         * Iterate through rawList and sort values into the correct list above
         */
        ////System.out.println("Initialising pointer to 0");
        int pointer = 0;
        for(int i=0; i<rawList.size(); i++) {
            ////System.out.println("i="+i+" rawList.size()="+rawList.size());
            try {
                // id, name, priv, owner -> 0

                if (pointer == 4) {
                    ////System.out.println("Pointer == 4");
                    ////System.out.println("Setting pointer to 0 again");
                    pointer = 0;
                } if (pointer == 0) {
                    ////System.out.println("Pointer == 0");
                    Integer id = Integer.parseInt(rawList.get(i));
                    ////System.out.println("rawList.get("+i+") returned "+rawList.get(i));
                    ////System.out.println("Adding this to idList");
                    idList.add(id);
                } if (pointer == 1) {
                    ////System.out.println("Pointer == 1");
                    ////System.out.println("rawList.get("+i+") returned "+rawList.get(i));
                    String name = rawList.get(i);
                    ////System.out.println("name="+name);
                    nameList.add(name);
                }if (pointer == 2) {
                    ////System.out.println("Pointer == 2");
                    ////System.out.println("rawList.get("+i+") returned "+rawList.get(i));
                    String privacy = rawList.get(i);
                    ////System.out.println("privacy="+privacy);
                    privacyList.add(privacy);
                }if (pointer == 3) {
                    ////System.out.println("Pointer == 3");
                    ////System.out.println("rawList.get("+i+") returned "+rawList.get(i));
                    Integer owner = Integer.parseInt(rawList.get(i));
                    ////System.out.println("owner="+owner);
                    ownerList.add(owner);
                }
            } catch (Exception e) {
                ////System.out.println("Didn't work! :/");
            }
            ////System.out.println("Pointer was "+pointer);
            pointer++;
            ////System.out.println("Pointer now "+pointer);
        }

        //debug prints
//        for (Integer in:idList) {
//            ////System.out.println("in="+in);
//        }
//
//        for (String str:nameList) {
//            ////System.out.println("str="+str);
//        }

        ////System.out.println("Playlist parsing complete");
        ////System.out.println("Generating playlist objects");
        for(int i=0; i<idList.size(); i++) {

            ////System.out.println("i="+i);
            ////System.out.println(
//                    "new Playlist("+idList.get(i)+","+nameList.get(i)+", "+privacyList.get(i)+", "+ownerList.get(i)+")");

            // Produce a Playlist object from the sorted data
            // A newly initialised Playlist has no Tracks inside it.
            Playlist playlist = new Playlist(idList.get(i),nameList.get(i),privacyList.get(i),ownerList.get(i));

            // let's check if there are any tracks in this playlist...
            if(playlist.isEmpty()) {
                // we have an empty playlist
                ////System.out.println("[Empty playlist found!]: "+playlist.toString());
                // however the user should still be able to add tracks in future
                // from the GUI.
            } else {
                // we need to display the Tracks in a TableView
                ////System.out.println("[Playlist with Tracks found!]: "+playlist.toString());
            }


            /* use the playlist id stored in the object to find tracks in this playlist.
             * These Tracks are then stored in an ObservableList<Track> that can be accessed through
              * playlist.getTracks();
             */

            playlist.loadContents();

            readyList.add(playlist);
            ////System.out.println("loadAll() is adding this Playlist object to readyList");
        }

//        Dysfunctional code...
//        ////System.out.println("rawList.size()-1: "+(rawList.size()-1));
//        int a,b,c,d;
//        a=0;b=1;c=2;d=3;
//        int incrementVal = 1;
//        while( d < (rawList.size()-1) ) {
//
//            try {
//                System.out.print("a=" + a + " b=" + b + " c=" + c + " d=" + d + "\n");
//                int id = Integer.parseInt(rawList.get(a));
//                ////System.out.println("[" + a + "] rawlist id: " + id);
//                String name = rawList.get(b);
//                ////System.out.println("rawlist name: " + name);
//                String privacy = rawList.get(c);
//                ////System.out.println("rawlist privacy: " + privacy);
//                int owner = Integer.parseInt(rawList.get(d));
//                ////System.out.println("rawlist owner: " + owner);
//                readyList.add(new Playlist(id, name, privacy, owner));
//                ////System.out.println("Playlist object toString(): " + readyList.get(a).toString());
//                ////System.out.println("END -> d=" + d);
//                a+=incrementVal;
//                b+=incrementVal;
//                c+=incrementVal;
//                d+=incrementVal;
//                ++incrementVal;
//            } catch (NumberFormatException e) {
//                ////System.out.println("Numberformatexception!");
//            }
        ////System.out.println("Populating readyList complete. Returning control from PlaylistManager.loadAll() to MainController.fetchPlaylist() ");
        return readyList;
    }

    public static ObservableList<String> showStored() {
        //ObservableList<String> headers = SQL.selectAllPlaylistNames();
        ObservableList<String> headers = SQL.selectAllFromPlaylist();
//        ////System.out.println("debug prints form showStored: ");
//        for (String pl:headers) {
//            ////System.out.println(pl);
//        }
        return headers;
    }

    // creates a playlist in default mode. Owner: logged in user, privacy: private.
    public void create(String name) {
        PlaylistFactory.setPrivacy("private");
        this.playlist = PlaylistFactory.create(name);
    }

    // overloaded method. For specifying privacy manually.
    public void create(String name, String privacy) {
        PlaylistFactory.setPrivacy(privacy);
        this.playlist = PlaylistFactory.create(name);
    }

    // Returns an ArrayList<Sequence> to TrackPlayer
    public ArrayList<Sequence> addToNowPlaying() {
        //NowPlaying.getInstance().addPlaylist2(); // arraylist of track ids as string
        return this.playlist.getSequence();
    }

    // shows the contents of selected playlist
    public void browse() {
        ArrayList<Sequence> seq = playlist.getSequence();
        // print contents of sequence...
    }

    // Adds a sequence of tracks to a playlist
    public void addSequence(ArrayList<Sequence> tracks) {
        playlist.addSequence(tracks);
    }

    // Pushes a single track object into a playlist
    public void pushTrack() {

    }

    // getter
    public Playlist getPlaylist(int id) {
        return this.playlist;
    }




    /*****************************************************
     * Gets saved playlists and creates Playlist objects *
     *****************************************************/
    public static ObservableList<Playlist> loadAllPlaylists() {
        ObservableList<Playlist> spawnedPlaylists = FXCollections.observableArrayList();
        int uid = Utilities.getUserID(Authenticator.getUser().getEmail());
        Connection c ;
        playlistData = FXCollections.observableArrayList();

        try{
            c = SQL.connect();
            ////System.out.println("PlaylistManager.loadAllPlaylists opened database");

            //SQL FOR SELECTING ALL PLAYLISTS
            String SQL = "SELECT * FROM PLAYLIST WHERE addedBy = " + uid +";";

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);
//            ObservableList<String> row = FXCollections.observableArrayList();

            while(rs.next()){
                Playlist playlist = new Playlist();

                playlist.setId(rs.getInt("id"));
                playlist.setName(rs.getString("name"));
                playlist.setType(rs.getString("type"));
                playlist.setOwnerId(rs.getInt("addedBy"));

                spawnedPlaylists.add(playlist);
            }

            rs.close();
            c.close();


            ////System.out.println("PlaylistManager.loadAllPlaylists closed database");


        }catch(Exception e){
            e.printStackTrace();
            ////System.out.println("Error on Building Data");
        }
        return spawnedPlaylists;
    }
}
