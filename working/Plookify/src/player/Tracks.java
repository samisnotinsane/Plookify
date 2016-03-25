package player;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Kristian André Aspevik on 29/01/16.
 */
public class Tracks {
    private String name;
    private int artistID;
    private String artistName;
    private String duration;
    private int durationSecs;
    private String genre;
    private String path;
    private String album;
    private String albumPath;
    private int id;

    public Tracks(int id, String name, String artist, int duration, String genre, String path, String album, String albumPath){
        this.name = name;
        this.artistName = artist;
        this.genre = genre;
        this.path = path;
        this.id = id;
        this.album = album;
        this.albumPath = albumPath;
        setDuration(duration);
    }

    public Tracks(int id, String name, String artist){
        this.name = name;
        this.artistName = artist;
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public int getArtistID(){
        return artistID;
    }

    public String getDuration(){
        return duration;
    }

    public String getGenre(){
        return genre;
    }

    public String getPath(){
        return path;
    }

    public int getID(){
        return id;
    }

    public String getArtistName(){
        return artistName;
    }

    public String getAlbum(){
        return album;
    }

    public String getAlbumPath(){
        return albumPath;
    }
    public void setDuration(int duration) {
        durationSecs = duration;
        NumberFormat formatter = new DecimalFormat("00");
        int minutes = duration/60;
        int seconds = duration % 60;
        String s = formatter.format(seconds);
        this.duration = "" + minutes + ":" + s;
    }

    public int getDurationSecs(){
        return durationSecs;
    }
}
