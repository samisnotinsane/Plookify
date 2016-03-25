package playlist.view;

import playlist.Playlist;

/**
 * Created by sameenislam on 23/03/2016.
 */
public class PlaylistStore {
    private static PlaylistStore ourInstance = new PlaylistStore();
    private Playlist playlist;

    public static PlaylistStore getInstance() {
        return ourInstance;
    }

    private PlaylistStore() {
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist pl){
        playlist = pl;
    }
}
