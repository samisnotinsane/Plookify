package playlist;

/**
 * Created by sameenislam on 16/02/2016.
 */
public class UnknownPrivacyException extends Exception {

    public UnknownPrivacyException() {

    }

    public UnknownPrivacyException(String privacy) {
        super(privacy);
    }
}
