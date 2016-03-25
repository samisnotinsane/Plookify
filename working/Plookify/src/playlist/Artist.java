package playlist;

import java.util.ArrayList;

/**
 * Created by sameenislam on 02/02/2016.
 * Represents a Track artist. Has a genre.
 * An Artist can only be added through a Track.
 */
public class Artist extends Sequence {
    private String name;
    private String genre;

    public Artist() {

    }

    // gets artist id for given name and uses that id to list all matching tracks
    public static ArrayList<String> browse(String query) {
        int aid = SQL.getIdForArtistName(query);
        ArrayList<String> tracksFound = SQL.getTracksByArtistId(aid);
        return tracksFound;
    }

    public void showAll() {
        ArrayList<String> artList = SQL.selectAllArtist();
        try {
            if(artList.get(0).equals(""))

                for(String metadata : artList) {
                    System.out.println(metadata);
                }
        } catch(IndexOutOfBoundsException e) {
            System.out.println("An error occurred while parsing artist names\n"+
                    "It's likely there are no artists in database\n"+
                    "Try adding an artist and try again");
        }
    }

    // searches TRACK and returns all tracks with the matching artist name
    public void search(String name) {
        ArrayList<String> trackHits = SQL.searchArtistByName(name);
        try {
            if(trackHits.get(0).equals("")) {}

                for(String track : trackHits) {
                    System.out.println(track);
                }
        } catch(IndexOutOfBoundsException e) {
            System.out.println("An error occurred while parsing!");
        }
    }

    public void create(String name) {
        this.name = name;
        boolean successful = SQL.insertIntoArtist(this.name);
        if(!successful)
            System.out.println("An error occurred when attempting to create new Track '"+this.name+"'");
        else {
            System.out.println("New artist '"+this.name+"' created successfully");
        }
    }

    protected String getName() {
        return this.name;
    }

    protected String getGenre() {
        return genre;
    }
}
