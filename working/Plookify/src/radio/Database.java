package radio;

import javafx.scene.control.Alert;

import java.sql.*;

/**
 * Created by nicks on 14/03/2016.
 */
public class Database {
    Connection c = null;
    Statement stmt = null;

    public void open() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            c.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println("Error opening database in the radio module");
        }
    }
    public String selectGenre(String artist)
    {
        String genreOfArtist = null;
        try {
            stmt = c.createStatement();
            ResultSet genreQuery = stmt.executeQuery("SELECT GENRE FROM TRACKS WHERE ARTIST ='" + artist + "'");
            genreOfArtist = genreQuery.getString("GENRE");
            genreQuery.close();
            stmt.close();
        }
        catch(java.sql.SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Artist not found!");
            alert.setContentText("The artist you tried to search for is not in our database, please try again.");

            alert.showAndWait();
        }
        return genreOfArtist;
    }
    public ResultSet getArtists(String genre, String artist)
    {
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM TRACKS WHERE GENRE  ='" + genre + "'" + "AND ARTIST != '" + artist + "'" + "order by RANDOM() limit 10");
            return rs;

        }
        catch(SQLException e)
        {
            System.out.println("Error getting artists from database ");
        }
        return rs;
    }
    public ResultSet ArtistQuery(String artistQuery)
    {
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT ARTIST FROM TRACKS WHERE ARTIST LIKE '%" + artistQuery + "%' COLLATE NOCASE order by ARTIST asc, NAME;");
            return rs;

        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("Error getting artists from database ");
        }
        return rs;
    }
    public void close()
    {
        try {
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("Error closing database in the radio module");
        }
    }

}
