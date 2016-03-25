package account;

/**
 * Created by tahnik on 17/02/2016.
 */
public class AuthenticatorTest {
    public static void AuthenticatorDBTest(){
        Authenticator authenticator = new Authenticator("tahnik@live.co.uk", "2682");
        System.out.println(authenticator.Authenticate());
        UserTest.printUser(Authenticator.getUser());
    }
}
