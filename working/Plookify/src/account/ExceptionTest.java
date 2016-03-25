package account;


/**
 * Created by tahnik on 01/03/2016.
 */
public class ExceptionTest {
    public static void testRegistrationException() throws RegistrationException{
        throw new RegistrationException("Hey There");
    }
}
