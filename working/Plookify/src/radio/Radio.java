package radio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

import java.util.*;
import java.util.Scanner;
public class Radio
{

    private ArrayList<String> resultsList;
    private String genre;
    private String artist;
    private Database database = new Database();

    public void search(String aName)
    {
        artist = aName;
        database.open();
        this.genre = database.selectGenre(artist);
        database.close();
    }

    public void randomGenerator()
    {
        ArrayList artists = new ArrayList<String>();
        try {
            database.open();
            ResultSet rs = database.getArtists(genre, artist);
            while ( rs.next() ) {
                String trackID = rs.getString("ID");
                String Newartist = rs.getString("ARTIST");
                String trackAlbum = rs.getString("ALBUM");
                String trackTime = rs.getString("DURATION");
                String track = rs.getString("NAME");
                String TrackArtist = trackID + " - " +track + " - " + Newartist + " - " + trackAlbum + " - " + trackTime;
                if(!(artists.contains(TrackArtist)))
                {
                    artists.add(TrackArtist);
                }


            }
            rs.close();
            database.close();

        } catch ( Exception e ) {
            System.err.println( "Error traversing ResultSet");
        }
        randomOutput(artists);
    }
    private void randomOutput(ArrayList<String> input)
    {
        ArrayList<String> output = new ArrayList<String>();
        int i = 0;
        while(i < input.size() && i < 10)
        {
            if (!(output.contains(input.get(i))))
                output.add(input.get(i));
            i++;
        }
        resultsList = output;
    }

    public ArrayList<String> getResults()
    {
      return resultsList;
    }
    public ObservableList<Songs> convertToObservableList()
    {
        final ObservableList<Songs> data = FXCollections.observableArrayList();
        for(String song : resultsList)
        {
            String[]songSplit = song.split(" - ");
            data.add(new Songs(songSplit[0],songSplit[1],songSplit[2],songSplit[3],songSplit[4],genre));
        }
        return data;
    }

public ArrayList<String> searchArtist(String artistQuery) {
            ArrayList artists = new ArrayList<String>();
            try {
                database.open();
                ResultSet rs = database.ArtistQuery(artistQuery);

                while ( rs.next() ) {
                    String artist = rs.getString("ARTIST");
                    if(!(artists.contains(artist))) {
                        artists.add(artist);
                    }
                }
                rs.close();
                database.close();
            } catch ( Exception e ) {
                e.printStackTrace();
                System.err.println( "Error traversing ResultSet");
            }
    return artists;
        }
}
