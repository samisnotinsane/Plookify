package social.logic;


import account.Authenticator;
import account.AuthenticatorTest;
import account.Utilities;

/**
 * Created by k1326476 on 06/03/2016.
 */
public class CUser {

    public static int Cuser = 0;


    public void Cuser() {
        // call a method from account ot get the current user


    }

    //get current user
    public static int getCuser() {
        setCuser();
        return Utilities.getUserID(Authenticator.getUser().getEmail());
    }

    public static void setCuser() {
        AuthenticatorTest.AuthenticatorDBTest();


    }


}