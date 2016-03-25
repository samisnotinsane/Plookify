package account;

import account.Utilities;

import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

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
        String createUser = "CREATE TABLE USER (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                "EMAIL VARCHAR (30) UNIQUE NOT NULL, PASSWORD VARCHAR (16))";
        String createUserDetails = "CREATE TABLE USERDETAILS (ID INTEGER PRIMARY KEY AUTOINCREMENT " +
                "UNIQUE NOT NULL, USERID INTEGER (3) REFERENCES USER (ID) ON DELETE CASCADE NOT NULL, FIRSTNAME " +
                "VARCHAR (20) NOT NULL, LASTNAME VARCHAR (20) NOT NULL)";
        String createUserStatus = "CREATE TABLE USERSTATUS (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT " +
                "UNIQUE, USERID INTEGER (3) NOT NULL REFERENCES USER (ID) ON DELETE CASCADE, STATUS BOOLEAN " +
                "NOT NULL)";
        String createAddress = "CREATE TABLE ADDRESS (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                "USERID INTEGER (3) REFERENCES USER (ID) ON DELETE CASCADE NOT NULL, ADDRESSLINE VARCHAR (50) " +
                "NOT NULL, CITY VARCHAR (50) NOT NULL, POSTCODE VARCHAR (8) NOT NULL)";
        String createUserSubscription = "CREATE TABLE USERSUBSCRIPTION (ID INTEGER PRIMARY KEY AUTOINCREMENT " +
                "NOT NULL UNIQUE, USERID INTEGER (3) REFERENCES USER (ID) ON DELETE CASCADE NOT NULL, " +
                "SUBSTYPE BOOLEAN NOT NULL)";
        String createPayment = "CREATE TABLE PAYMENT (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                "SUBSID INTEGER (3) NOT NULL REFERENCES USERSUBSCRIPTION (ID) ON DELETE CASCADE, PAYMENTMETHOD " +
                "INTEGER (3) NOT NULL, NEXTPAYMENTDATE BIGINT (15) NOT NULL);";
        String createPaypal = "CREATE TABLE PAYPAL (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
                "PAYMENTID INTEGER (3) REFERENCES PAYMENT (ID) ON DELETE CASCADE NOT NULL, EMAIL VARCHAR (50) " +
                "NOT NULL, PASSWORD VARCHAR (40) NOT NULL)";
        String createCreditCard = "CREATE TABLE CREDITCARD (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE " +
                "NOT NULL, PAYMENTID INTEGER (3) REFERENCES PAYMENT (ID) ON DELETE CASCADE NOT NULL, LONGDIGIT " +
                "INTEGER (16) NOT NULL, CSV INTEGER (3) NOT NULL, EXPIRYDATE INTEGER (15) NOT NULL)";
        String createDiscoverableTable = "CREATE TABLE disctbl (disc_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE" +
                "NOT NULL, user_id INTEGER REFERENCES USER (ID) ON DELETE CASCADE" +
                "NOT NULL, disc INTEGER NOT NULL)";
        String createDevicesTable = "CREATE TABLE DEVICES (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                "USERID INTEGER REFERENCES USER (ID) ON DELETE CASCADE NOT NULL, NAME VARCHAR (50) NOT NULL, " +
                "ADDEDAT BIGINT (20) NOT NULL, TYPE INTEGER (3) NOT NULL)";
        String statements[] = {createAddress, createCreditCard, createPayment, createPaypal, createUser,
                                createUserDetails, createUserStatus, createUserSubscription};
        ArrayList<String> query = new ArrayList<>(Arrays.asList(statements));
        Utilities.executeMultiQuery(query);
    }
}
