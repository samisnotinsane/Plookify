package account;

import java.util.ArrayList;

/**
 * Created by tahnik on 14/02/2016.
 */
public class UserTest {
    static User user;
    public static void UserObjectTest(){
        user = new User();
        user.setFirstName("Tahnik");
        user.setLastName("Mustasin");
        user.setEmail("tahnik@live.co.us");
        user.setAddressLine("12 Francis Avenue");
        user.setCity("Ilford");
        user.setPostCode("IG1 1TS");
        user.setPassword("testing");
        user.setSubscriptionType(false);
        user.setStatus(true);
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getEmail());
        System.out.println(user.getAddressLine());
        System.out.println(user.getCity());
        System.out.println(user.getPostCode());
        System.out.println(user.getSubscriptionType());
        System.out.println(user.getStatus());
        System.out.println(user.getPassword());
    }
    public static void UserDatabaseTest(){
        UserObjectTest();
        String singleQuery;
        singleQuery = "INSERT INTO USER(EMAIL, PASSWORD) VALUES('" + user.getEmail() + "','" + user.getPassword()
                            + "')";
        Utilities.executeSingleQuery(singleQuery);
        /*
        User must be created First before executing other queries involving Foreign ID
        Couldn't use Utilities.getUserID before creating user
         */
        singleQuery = "INSERT INTO USERDETAILS(USERID, FIRSTNAME, LASTNAME) VALUES('" + Utilities.getUserID(user.getEmail())
                + "','" + user.getFirstName() + "','" + user.getLastName() + "')";
        Utilities.executeSingleQuery(singleQuery);
        singleQuery = "INSERT INTO USERSUBSCRIPTION(USERID,SUBSTYPE) VALUES('" + Utilities.getUserID(user.getEmail())
                        + "','" + user.getSubscriptionType() + "')";
        Utilities.executeSingleQuery(singleQuery);
        singleQuery = "INSERT INTO USERSTATUS(USERID, STATUS) VALUES('" + Utilities.getUserID(user.getEmail())
                        + "','" + user.getStatus() + "')";
        Utilities.executeSingleQuery(singleQuery);
        singleQuery = "INSERT INTO ADDRESS(USERID, ADDRESSLINE, CITY, POSTCODE) VALUES('"
                        + Utilities.getUserID(user.getEmail()) + "','"
                        + user.getAddressLine() + "','"
                        + user.getCity() + "','"
                        + user.getPostCode() + "')";
    }
    protected static void printUser(User userp){
        user = userp;
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getEmail());
        System.out.println(user.getAddressLine());
        System.out.println(user.getCity());
        System.out.println(user.getPostCode());
        System.out.println(user.getSubscriptionType());
        System.out.println(user.getStatus());
        System.out.println(user.getPassword());
    }
}
