package playlist;

/**
 * Created by sameenislam on 13/02/2016.
 */
public class User {

    private static int uid; // current user id
    private static String username; // email address of user

    // returns true on successful username & password combo
    public static boolean authenticate(String email, String password) {
        boolean pwdIsIn = SQL.matchUserPassword(email, password); // match password for given username
        if(pwdIsIn) {
            username = email;
            // get id from USER and store it in id.
            uid = SQL.getUid(email,password);
            return true;
        } else {return false;}
    }

    // returns the ID of whoever authenticated
    public static int getId() {
        return uid;
    }

    /* Authentication. Normal flow:
     * 1. Enter username and password
     * 2. Check database for match
     * 3. Fetch userId and store in class variable
     */
}
