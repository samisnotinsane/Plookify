package playlist;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by sameenislam on 16/02/2016.
 * Factory method for creating a new Playlist object.
 */
public class PlaylistFactory {

    private static String privacy;

    protected static Playlist create(String name) {
        int oid = User.getId();
        return new Playlist(name, oid, privacy);
    }

    // load stored playlist object into memory, identified by id
    public static Playlist load(int id) {
        // fetch matching playlist record from database
        ArrayList<String> pHead = SQL.getPlaylistHeader(id); // return format: |id|name|type|addedBy|
        int matchingId = Integer.parseInt(pHead.get(0));
        String plName = pHead.get(1);
        String plType = pHead.get(2);
        int plOwner = Integer.parseInt(pHead.get(3));
        return new Playlist(matchingId, plName, plType, plOwner);
    }

    protected static void setPrivacy(String privacy) {
        try {
            if(privacy.equalsIgnoreCase("private")) {
                PlaylistFactory.privacy = "private";
            } else if (privacy.equalsIgnoreCase("friend")) {
                PlaylistFactory.privacy = "friend";
            } else
                throw new UnknownPrivacyException(privacy);
        } catch (UnknownPrivacyException e) {
            System.out.println("Illegal privacy type! Please set to either 'private' or 'friend'.\n"+e.toString());
        }
    }
}
