package playlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sameenislam on 11/02/2016.
 *
 * Utility class for database operations.
 */
public class SQL {

    public static ObservableList<Integer> trackIdList = FXCollections.observableArrayList();
    ObservableList<Integer> playlistIdList = FXCollections.observableArrayList();
    ObservableList<Integer> sequenceIdList = FXCollections.observableArrayList();
    ObservableList<Integer> pltrkIdList = FXCollections.observableArrayList();

    private static Connection conn = null;
    private static Statement stmt  = null;

    private static String url = "jdbc:sqlite:bin/test.db";
    private static String user = "root";
    private static String pass = "root";

    // Universal methods for accessing db through other classes
    public static Connection connect() throws SQLException{
        try{
            Class.forName("org.sqlite.JDBC").newInstance();
        }catch(ClassNotFoundException cnfe){
            System.err.println("Error: "+cnfe.getMessage());
        }catch(InstantiationException ie){
            System.err.println("Error: "+ie.getMessage());
        }catch(IllegalAccessException iae){
            System.err.println("Error: "+iae.getMessage());
        }

        conn = DriverManager.getConnection(url,user,pass);
        return conn;
    }
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        if(conn !=null && !conn.isClosed())
            return conn;
        connect();
        return conn;

    }

    public ObservableList<Integer> getTrackIdList() {
        return trackIdList;
    }

    public ObservableList<Integer> getPlaylistIdList() {
        return playlistIdList;
    }

    public ObservableList<Integer> getSequenceIdList() {
        return sequenceIdList;
    }

    public ObservableList<Integer> getPlTrkList() {
        return pltrkIdList;
    }

    // -----------------------------------------------------------------

    // Returns all rows from Playlist table
    public static ObservableList<String> selectAllFromPlaylist() {

        // |---id---|---name---|---type---|---addedBy---|
        ObservableList<String> returnSet = FXCollections.observableArrayList();

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("selectAllFromPlaylist opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYLIST;");

            while ( rs.next() ) {
                String id = Integer.toString(rs.getInt("id"));
                String  name = rs.getString("name");
                String type = rs.getString("type");
                String  addedBy = rs.getString("addedBy");

                returnSet.addAll(id, name, type, addedBy);
//              debug prints
//                //System.out.println( "ID = " + id );
//                //System.out.println( "NAME = " + name );
//                //System.out.println( "TYPE = " + type );
//                //System.out.println( "ADDED BY = " + addedBy );
//                //System.out.println();

//                String[] insArray = new String[4];
//                insArray[0] = Integer.toString(id);
//                insArray[1] = name;
//                insArray[2] = type;
//                insArray[3] = addedBy;
//
//                for(int i=0; i<insArray.length; i++) {
//                    returnSet.add(insArray[i]);
//                }
            }
            rs.close();
            stmt.close();
            conn.close();
            //System.out.println("selectAllFromPlaylist closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A fatal error occurred! Terminating...");
            System.exit(0);
        }
        return returnSet;
    }

    // returns matching row of playlist header. Matched by id
    public static ArrayList<String> getPlaylistHeader(int targetId) {

        // |---id---|---name---|---type---|---addedBy---|
        ArrayList<String> returnSet = new ArrayList<String>();

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("getPlaylistHeader opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYLIST WHERE ID = " + targetId + ";");

            rs.next();
            int id = rs.getInt("id");
            String  name = rs.getString("name");
            String type = rs.getString("type");
            String  addedBy = rs.getString("addedBy");

            String[] insArray = new String[4];
            insArray[0] = Integer.toString(id);
            insArray[1] = name;
            insArray[2] = type;
            insArray[3] = addedBy;

            for(int i=0; i<insArray.length; i++) {
                returnSet.add(insArray[i]);
            }

            rs.close();
            stmt.close();
            conn.close();
            //System.out.println("getPlaylistHeader closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A fatal error occurred! Terminating...");
            System.exit(0);
        }
        return returnSet;
    }


    public static ObservableList<String> selectAllPlaylistNames() {

        // |---id---|---name---|---type---|---addedBy---|
        ObservableList<String> returnSet = FXCollections.observableArrayList();

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("selectAllPlaylistNames opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM PLAYLIST;");

            while ( rs.next() ) {

                String  name = rs.getString("name");

//              debug prints
//              //System.out.println( "NAME = " + name );
//              //System.out.println();
                returnSet.add(name);
            }
            rs.close();
            stmt.close();
            conn.close();
            //System.out.println("selectAllPlaylistNames closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A fatal error occurred! Terminating...");
            System.exit(0);
        }
        return returnSet;
    }

    // Returns all rows from PlaylistTracks table
    public static ArrayList<String> selectFromPlaylistTrack() {
        // |---id---|---name---|---type---|---addedBy---|
        ArrayList<String> returnSet = new ArrayList<String>();

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("selectFromPlaylistTrack opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYLISTTRACK;");

            while ( rs.next() ) {
                int id = rs.getInt("id");
                String  trackid = rs.getString("trackid");
                String playlistId = rs.getString("playlistId");
                String  trackSequenceNo = rs.getString("trackSequenceNo");

                // debug prints
                //System.out.println( "ID = " + id );
                //System.out.println( "trackid = " + trackid );
                //System.out.println( "playlistId = " + playlistId );
                //System.out.println( "trackSequenceNo = " + trackSequenceNo );
                //System.out.println();

                String[] insArray = new String[4];
                insArray[0] = Integer.toString(id);
                insArray[1] = trackid;
                insArray[2] = playlistId;
                insArray[3] = trackSequenceNo;

                for(int i=0; i<insArray.length; i++) {
                    returnSet.add(insArray[i]);
                }
            }
            rs.close();
            stmt.close();
            conn.close();
            //System.out.println("selectFromPlaylistTrack closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A fatal error occurred! Terminating...");
            System.exit(0);
        }
        return returnSet;
    }

    public static ResultSet selectAllTrack() {
        // |---id---|---name---|---artistId---|---duration---|---genreId---|---path---|
        //ArrayList<String> returnSet = new ArrayList<String>();
        ResultSet returnSet = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("selectAllTrack opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TRACK;");

            while ( rs.next() ) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int artistId = rs.getInt("artistId");
                int duration = rs.getInt("duration");
                int genreId = rs.getInt("genreId");
                String path = rs.getString("path");

                // debug prints
                //System.out.println( "ID = " + id );
                //System.out.println( "name = " + name );
                //System.out.println( "artistId = " + artistId );
                //System.out.println( "duration = " + duration );
                //System.out.println( "genreId = " + genreId );
                //System.out.println( "path = " + path );
                //System.out.println();

//                String[] insArray = new String[6];
//                insArray[0] = Integer.toString(id);
//                insArray[1] = name;
//                insArray[2] = Integer.toString(artistId);
//                insArray[3] = Integer.toString(duration);
//                insArray[4] = Integer.toString(genreId);
//                insArray[5] = path;

//                for(int i=0; i<insArray.length; i++) {
//                    returnSet.add(insArray[i]);
//                }
            }
            rs.close();
            stmt.close();
            conn.close();
            //System.out.println("selectAllTrack closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A fatal error occurred! Terminating...");
            System.exit(0);
        }
        return returnSet;
    }


    public static boolean insertIntoPlaylist(String name, String type, int addedBy) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("SQL.insertIntoPlaylist opened database");
            stmt = conn.createStatement();

            String sql = "INSERT INTO playlist(name, type, addedBy) VALUES (" +
                    "'" + name + "'" + "," +
                    "'" + type + "'" + "," +
                    "'" + addedBy + "'" +
                    ");";

            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            //System.out.println("SQL.insertIntoPlaylist closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            return false;
        }
        return true;
    }



    public static boolean insertIntoPlaylistTrack(int trackId, String playlistId, String trackSequenceNo) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("insertIntoPlaylistTrack opened database");
            stmt = conn.createStatement();

            String sql = "INSERT INTO PLAYLISTTRACK(trackId, playlistId, trackSequenceNo) VALUES (" +
                    "'" + trackId + "'" + "," +
                    "'" + playlistId + "'" + "," +
                    "'" + trackSequenceNo + "'" +
                    ");";

            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            //System.out.println("insertIntoPlaylistTrack closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            return false;
        }
        return true;
    }

    public static boolean insertIntoTrack(String name, String artistId, String duration, String genreId, String path) {
        try {
            int aId = Integer.parseInt(artistId);
            int dur = Integer.parseInt(duration);
            int gId = Integer.parseInt(genreId);


            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("insertIntoTrack opened database");
            stmt = conn.createStatement();

            String sql = "INSERT INTO TRACK(NAME, ARTISTID, DURATION, GENREID, PATH) VALUES (" +
                    "'" + name + "'" + "," +
                    "'" + aId + "'" + "," +
                    "'" + dur + "'" + "," +
                    "'" + gId + "'" + "," +
                    "'" + path + "'" +
                    ");";

            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            //System.out.println("insertIntoTrack closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            return false;
        }
        return true;
    }

    public static boolean insertIntoArtist(String name) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("insertIntoArtist opened database");
            stmt = conn.createStatement();

            String sql = "INSERT INTO ARTIST(NAME) VALUES (" +
                    "'" + name + "'" +
                    ");";

            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            //System.out.println("insertIntoArtist closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            return false;
        }
        return true;
    }

    public static ArrayList<String> selectAllArtist() {
        // |---id---|---name---|
        ArrayList<String> returnSet = new ArrayList<String>();

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("selectAllArtist opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ARTIST;");

            while ( rs.next() ) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                // debug prints
                //System.out.println( "ID = " + id );
                //System.out.println( "name = " + name );
                //System.out.println();

                String[] insArray = new String[2];
                insArray[0] = Integer.toString(id);
                insArray[1] = name;

                for(int i=0; i<insArray.length; i++) {
                    returnSet.add(insArray[i]);
                }
            }
            rs.close();
            stmt.close();
            conn.close();
            //System.out.println("selectAllArtist closed database");
        } catch(Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A fatal error occurred!");
        }
        return returnSet;
    }


    public static boolean changePlaylistName(int playlistid, String newName) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("changePlaylistName opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYLIST WHERE id="+"'"+playlistid+"'"+";");

            rs.next();

            int id = rs.getInt("id");
            String name = rs.getString("name");

            //System.out.println("Playlist id '"+playlistid+"' corresponds to '"+name+"'"+
//            " in database.");
            //System.out.println("Attempting to set name to '"+newName+"'");

            String sql = "UPDATE playlist SET name = '"+ newName + "' WHERE ID = "+ playlistid;

            stmt.executeUpdate(sql);

            rs = stmt.executeQuery("SELECT * FROM PLAYLIST WHERE id="+"'"+playlistid+"'"+";");
            rs.next();
            id = rs.getInt("id");
            name = rs.getString("name");
            //System.out.println("ID '"+id+"' now corresponds to '"+name+"'");

            rs.close();
            stmt.close();
            conn.close();
            //System.out.println("changePlaylistName closed database");


        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A fatal error occurred! Terminating...");
            return false;
        }
        return true;
    }

    public static int getUid(String email, String password) {
        int id=0;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("getUid opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM USER WHERE email = " + "'" + email + "'" +
                    "AND" + " password = " +
                    "'" + password + "'" +
                    ";");
            rs.next();

            id = rs.getInt("id");
            //System.out.println("Fetched uid: "+id);
            rs.close();
            stmt.close();
            conn.close();
            //System.out.println("getUid closed database");
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A fatal error occurred!");
        }
        return id;
    }

    public static boolean matchUserPassword(String email, String password) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("matchUserPassword opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM USER WHERE email = " + "'" + email + "'" +
                    "AND" + " password = " +
                    "'" + password + "'" +
                    ";");
            rs.next();

            //System.out.println("matchUserPassword closed database");

            String fetchedEmail = rs.getString("email");
            String fetchedPassword = rs.getString("password");

            if(fetchedEmail.equals(email) && fetchedPassword.equals(password)) {
                // correct username/password combo
                rs.close();
                stmt.close();
                conn.close();
                return true;
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A critical error occurred!");
        }
        return false;
    }

    public static int getIdForArtistName(String query) {
        int id = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("getUid opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ARTIST WHERE NAME = LIKE " +
                    "%" + query + "%" + ";");
            rs.next();
            id = rs.getInt("id");
            //System.out.println("Fetched aid: "+id);
            rs.close();
            stmt.close();
            conn.close();
            //System.out.println("getUid closed database");
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+e.getMessage() );
            //System.out.println("A fatal error occurred!");
        }
        return id;

    }

    // return all tracks with the matching artist id
    public static ArrayList<String> getTracksByArtistId(int aid) {
        return new ArrayList<String>();
    }

    // returns tracks that have the matching artist name
    // called by search() in Artist.java
    public static ArrayList<String> searchArtistByName(String name) {
        ArrayList<String> returnSet = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("searchArtistByName opened database");

            stmt = conn.createStatement();
            // first we have to determine the corresponding artist id
            ResultSet rs = stmt.executeQuery("SELECT ID FROM ARTIST WHERE NAME LIKE " + "'" + name + "'" + ";");
            rs.next();
            int aidHit = rs.getInt("ID");
            //System.out.println("Artist ID = "+aidHit);
            rs.close();

            // now list all records in TRACK with matching ARTISTID
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM TRACK WHERE ARTISTID = " + aidHit + ";");
            while (rs2.next()) {
                int id = rs2.getInt("ID");
                String tName = rs2.getString("NAME");
                int aid = rs2.getInt("ARTISTID");
                int tDur = rs2.getInt("DURATION");
                int genreID = rs2.getInt("GENREID");
                String path = rs2.getString("PATH");

                returnSet.add("Track ID: "+Integer.toString(id));
                returnSet.add("Track name: "+tName);
                returnSet.add("Artist ID: "+Integer.toString(aid));
                returnSet.add("Duration: "+Integer.toString(tDur));
                returnSet.add("Genre ID: "+Integer.toString(genreID));
                returnSet.add(path);
            }

            // closing connection
            stmt.close();
            conn.close();
            // ---

            //System.out.println("searchArtistByName closed database");

        } catch (Exception e) {
            //System.out.println(e.getClass().getName() + ": " + e.getMessage() );
        }
        return returnSet;
    }

    // delete header from PLAYLIST table
    public static boolean deleteHeaderFromPlaylist(int id) {
        try {

            ////System.out.println(id);
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("deleteHeaderFromPlaylist opened database");

            stmt = conn.createStatement();

            String sql = "DELETE FROM PLAYLIST WHERE ID=" +
                    "'" + id + "'" +
                    ";";
            stmt.executeUpdate(sql);

            // closing connection
            stmt.close();
            conn.close();

            //System.out.println("deleteHeaderFromPlaylist closed database");

        } catch (Exception e) {
            //System.out.println("Exception occurred in deleteHeaderFromPlaylist");
            return false;
        }
        return true;
    }


    public static int returnLastPlaylistId() {
        Integer tblid = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("returnLastPlaylistId opened database");
            stmt = conn.createStatement();

            String sql = "SELECT * FROM TABLE WHERE ID = (SELECT MAX(ID) FROM TABLE);";

            ResultSet rs = stmt.executeQuery(sql); // selects in ascending order
            tblid = rs.getInt("TEMP");
            //System.out.println("TEMP="+tblid);

            rs.close();
            // closing connection
            stmt.close();
            conn.close();
            // ---

            //System.out.println("returnLastPlaylistId closed database");

        } catch (Exception e) {
            //System.out.println(e.getClass().getName() + ": " + e.getMessage() );
        }
        return  tblid + 1;
    }

    public static void removePlaylist(int id) {

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("removePlaylist opened database");
            stmt = conn.createStatement();

            String sql = "DELETE FROM playlist WHERE id="+id+";";

            stmt.executeQuery(sql); // selects in ascending order

            // closing connection
            stmt.close();
            conn.close();
            // ---

            //System.out.println("removePlaylist closed database");

        } catch (Exception e) {
            //System.out.println(e.getClass().getName() + ": " + e.getMessage() );
        }

    }


    public static void renamePlaylist(String name, int id) {

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("renamePlaylist opened database");
            stmt = conn.createStatement();

            String sql = "UPDATE PLAYLIST SET NAME = '"+ name+"' WHERE id='"+id+"';";

            stmt.executeQuery(sql); // selects in ascending order

            // closing connection
            stmt.close();
            conn.close();
            // ---

            //System.out.println("renamePlaylist closed database");

        } catch (Exception e) {
            //System.out.println(e.getClass().getName() + ": " + e.getMessage() );
        }

    }


    /*
     * ///////////////Playlist Tracks/////////////////
     */

    public void selectTracksMatchingPlaylist(int id) {
        ObservableList<Integer> returnSet = FXCollections.observableArrayList();
        //System.out.println("Searching for track ids that correspond to playlist id "+id);
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("selectTracksMatchingPlaylist opened database");
            stmt = conn.createStatement();
            // first we have to determine the corresponding artist id
            ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYLISTTRACK WHERE PLAYLISTID = " + "'" + id + "'"
                    + " ORDER BY TRACKSEQUENCENO ASC" + ";"); // selects in ascending order

            boolean isEmpty = playlistTrackEmptyChecker(id);
            //System.out.println("playlistTrackEmptyChecker for playlist id "+id
//            + " returned "+ isEmpty);

            if(!isEmpty) {
                //System.out.println("There are no tracks in this playlist!");
//                //System.out.println("Adding 0 to returnSet and returning");
                //returnSet.add(0);
                //return returnSet;
            }
            //System.out.println("Tracks found in playlist "+id);
            while(rs.next()) {
            Integer tblid = rs.getInt("ID");
            //System.out.println("Found a track with trackId="+tblid+"!");
            int trkid = rs.getInt("TRACKID");
            int plid = rs.getInt("PLAYLISTID");
            int seqid = rs.getInt("TRACKSEQUENCENO");
            pltrkIdList.add(tblid);
            trackIdList.add(trkid);
            playlistIdList.add(plid);
            sequenceIdList.add(seqid);

            //returnSet.addAll(tblid, trkid, plid, seqid);
            }


            rs.close();
            // closing connection
            stmt.close();
            conn.close();
            // ---

            //System.out.println("selectTracksMatchingPlaylist closed database");

        } catch (Exception e) {
            //System.out.println(e.getClass().getName() + ": " + e.getMessage() );
        }
//        return returnSet;
        // method tested and passed on 20/03/16 - needs parser for support.
    }

    private static boolean playlistTrackEmptyChecker (int playlistID) {
        int listSize = 0;
        boolean doesExist = false;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS LIST FROM PLAYLISTTRACK WHERE PLAYLISTID ="+playlistID);
            listSize = rs.getInt("LIST");
            //System.out.println("listSize="+listSize);
            if(listSize != 0) {
                doesExist = true;
            }
        } catch (Exception e) {
            //System.out.println("Exception faced in playlistTrackEmptyChecker!");
        }
        return doesExist;
    }


    public static ObservableList<String> loadTrackById(int id) {
        //System.out.println("loadTrackById received id="+id);
        ObservableList<String> trackRecord = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            //System.out.println("loadTrackById opened database");

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TRACKS WHERE ID = " + "'" + id + "'" + ";");
            while(rs.next()) {

                int trackId = rs.getInt("ID");
                String trackName = rs.getString("NAME");
                String trackArtist = rs.getString("ARTIST");
                int trackDuration = rs.getInt("DURATION");

                trackRecord.addAll(Integer.toString(trackId), trackName, trackArtist, Integer.toString(trackDuration));
            }
            rs.close();

            // closing connection
            stmt.close();
            conn.close();
            // ---

            //System.out.println("loadTrackById closed database");

        } catch (Exception e) {
            //System.out.println("loadTrackById faced exception while executing");
            //System.out.println(e.getClass().getName() + ": " + e.getMessage() );
        }
        //System.out.println("loadTrackById is printing trackRecords:");
        for(String str : trackRecord) {
            //System.out.println(str);
        }

        return trackRecord;
    }
} // END SQL