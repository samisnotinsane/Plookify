package playlist;

/**
 * Created by sameenislam on 24/03/2016.
 * Data model for TableView inside Main.fxml
 */
public class ObservableTrack {
    Integer sequenceNumber = 0;
    Track track;

    public ObservableTrack(Track trk) {
        sequenceNumber++;
        track = trk;
    }


}
