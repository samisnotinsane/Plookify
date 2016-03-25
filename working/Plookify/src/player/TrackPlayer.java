package player;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Created by Kristian on 09/02/16.
 */
public class TrackPlayer {

    public static TrackPlayer trackPlayer = new TrackPlayer();
    private MediaPlayer mediaPlayer = null;
    private Media media = null;

    private TrackPlayer(){

    }

    public static TrackPlayer getInstance(){
        return trackPlayer;
    }
}
