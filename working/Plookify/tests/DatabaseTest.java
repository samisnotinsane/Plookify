package account.UnitTest;

import account.Utilities;

import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

/**
 * Created by tahnik on 07/02/2016.
 * This class will be the unit test for DB
 */
public class DatabaseTest {
    public static void testGetData(){
        System.out.println("I was here GetData");
    }
    public static void testSetData(){
        System.out.println("I was here SetData");
        String createUser = "CREATE TABLE USER (ID INTEGER (3) PRIMARY KEY UNIQUE NOT NULL, EMAIL VARCHAR (30) " +
                "UNIQUE NOT NULL, PASSWORD VARCHAR (16))";
        String createUserDetails = "CREATE TABLE USERDETAILS (ID INTEGER (3) PRIMARY KEY UNIQUE NOT NULL, USERID " +
                "INTEGER (3) REFERENCES USER (ID) ON DELETE CASCADE NOT NULL, FIRSTNAME VARCHAR (20) NOT NULL, " +
                "LASTNAME VARCHAR (20) NOT NULL)";
        String createUserStatus = "CREATE TABLE USERSTATUS (ID INTEGER (3) NOT NULL PRIMARY KEY UNIQUE, " +
                "USERID INTEGER (3) NOT NULL REFERENCES USER (ID) ON DELETE CASCADE, STATUS INTEGER (3) NOT NULL)";
        String createAddress = "CREATE TABLE ADDRESS (ID INTEGER (3) PRIMARY KEY UNIQUE NOT NULL, USERID INTEGER (3) " +
                "REFERENCES USER (ID) ON DELETE CASCADE NOT NULL, ADDRESSLINE VARCHAR (50) NOT NULL, " +
                "CITY VARCHAR (50) NOT NULL, POSTCODE VARCHAR (8) NOT NULL)";
        String createUserSubscription = "CREATE TABLE USERSUBSCRIPTION (ID INTEGER (3) NOT NULL PRIMARY KEY UNIQUE, " +
                "USERID INTEGER (3) REFERENCES USER (ID) ON DELETE CASCADE NOT NULL, SUBSTYPE INTEGER (1) NOT NULL)";
        String createPayment = "CREATE TABLE PAYMENT (ID INTEGER (3) PRIMARY KEY UNIQUE NOT NULL, SUBSID INTEGER (3) " +
                "NOT NULL REFERENCES USERSUBSCRIPTION (ID) ON DELETE CASCADE, PAYMENTMETHOD INTEGER (3) NOT NULL, " +
                "NEXTPAYMENTDATE INTEGER (10) NOT NULL);";
        String createPaypal = "CREATE TABLE PAYPAL (ID INTEGER (3) PRIMARY KEY NOT NULL UNIQUE, PAYMENTID " +
                "INTEGER (3) REFERENCES PAYMENT (ID) ON DELETE CASCADE NOT NULL, EMAIL VARCHAR (50) " +
                "UNIQUE NOT NULL, PASSWORD VARCHAR (40) UNIQUE NOT NULL)";
        String createCreditCard = "CREATE TABLE CREDITCARD (ID INTEGER (3) PRIMARY KEY UNIQUE NOT NULL, " +
                "PAYMENTID INTEGER (3) REFERENCES PAYMENT (ID) ON DELETE CASCADE NOT NULL, LONGDIGIT INTEGER (16) " +
                "UNIQUE NOT NULL, CSV INTEGER (3) NOT NULL, EXPIRYDATE INTEGER (5) NOT NULL)";
        String statements[] = {createAddress, createCreditCard, createPayment, createPaypal, createUser,
                createUserDetails, createUserStatus, createUserSubscription};
        Utilities.executeQuery(statements);
    }
}
