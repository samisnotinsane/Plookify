package radio;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by nicks on 13/03/2016.
 */
public class Songs {
    private final SimpleStringProperty trackID;
    private final SimpleStringProperty trackName;
    private final SimpleStringProperty trackArtist;
    private final SimpleStringProperty trackAlbum;
    private final SimpleStringProperty trackTime;
    private final SimpleStringProperty trackGenre;



    public Songs(String trackID, String trackName, String trackArtist,String trackAlbum, String trackTime, String trackGenre) {
        this.trackID = new SimpleStringProperty(trackID);
        this.trackName = new SimpleStringProperty(trackName);
        this.trackArtist = new SimpleStringProperty(trackArtist);
        this.trackAlbum = new SimpleStringProperty(trackAlbum);
        this.trackTime = new SimpleStringProperty(trackTime);
        this.trackGenre = new SimpleStringProperty(trackGenre);

    }

    public String getTrackID() {
        return trackID.get();
    }
    public void setTrackID(String traID) {
        trackID.set(traID);
    }

    public String getTrackName() {
        return trackName.get();
    }
    public void setTrackName(String tName) {
        trackName.set(tName);
    }

    public String getTrackArtist() {
        return trackArtist.get();
    }
    public void setTrackArtist(String tArtist) {
        trackArtist.set(tArtist);
    }

    public String getTrackAlbum() {
        return trackAlbum.get();
    }


    public String getTrackTime() {
        int minutes = (Integer.parseInt(trackTime.get()) % 3600) / 60;
        int seconds = (Integer.parseInt(trackTime.get())) % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    public void setTrackAlbum(String trackAlbum) {
        this.trackAlbum.set(trackAlbum);
    }

    public void setTrackTime(String trackTime) {
        this.trackTime.set(trackTime);
    }

    public String getTrackGenre() {
        return trackGenre.get();
    }

    public void setTrackGenre(String trackGenre) {
        this.trackGenre.set(trackGenre);
    }

}
