package account;

import com.sun.deploy.net.protocol.about.AboutURLConnection;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.runtime.SystemProperties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import javax.jws.soap.SOAPBinding;
import javax.print.attribute.standard.MediaSize;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;

/**
 * Created by tahnik on 07/02/2016.
 * This will be utilities class for holding the database create and etc
 */

public class Utilities {
    public static void databaseCreator(){
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while(rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        }
        catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }
    public static void executeMultiQuery(ArrayList<String> statements){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            for (String args : statements) {
                statement.executeUpdate(args);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
    }
    public static void executeSingleQuery(String query){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
    }
    public static User getUser(String email){
        Connection connection = null;
        User user = null;
        String query;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            query = "SELECT * FROM USER WHERE EMAIL='" + email + "'";
            ResultSet rs = statement.executeQuery(query);
            user = new User();
            user.setEmail(rs.getString("EMAIL"));
            user.setPassword(rs.getString("PASSWORD"));
            rs.close();

            query = "SELECT * FROM USERDETAILS WHERE USERID='" + Utilities.getUserID(email) + "'";
            rs = statement.executeQuery(query);
            user.setFirstName(rs.getString("FIRSTNAME"));
            user.setLastName(rs.getString("LASTNAME"));
            rs.close();
            query = "SELECT * FROM ADDRESS WHERE USERID='" + Utilities.getUserID(email) + "'";
            rs = statement.executeQuery(query);
            user.setAddressLine(rs.getString("ADDRESSLINE"));
            user.setCity(rs.getString("CITY"));
            user.setPostCode(rs.getString("POSTCODE"));
            rs.close();
            query = "SELECT STATUS FROM USERSTATUS WHERE USERID='" + Utilities.getUserID(email) + "'";
            rs = statement.executeQuery(query);
            user.setStatus(rs.getBoolean("STATUS"));
            rs.close();
            query = "SELECT SUBSTYPE FROM USERSUBSCRIPTION WHERE USERID ='" + Utilities.getUserID(email) + "'";
            rs = statement.executeQuery(query);
            user.setSubscriptionType(rs.getBoolean("SUBSTYPE"));
            rs.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return user;
    }

    /**
     * Uses email to find the ID of a user
     * @param email
     * @return id
     */
    public static int getUserID(String email){
        Connection connection = null;
        int id = -1;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT ID FROM USER WHERE EMAIL='" + email + "'";
            ResultSet rs = statement.executeQuery(query);
            id = rs.getInt("ID");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return id;
    }

    /**
     * @param email
     * @return subID
     */
    public static int getSubscriptionID(String email){
        //System.out.println("Entering Subscription ID");
        Connection connection = null;
        int subID = -1;
        int userID = Utilities.getUserID(email);
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT ID FROM USERSUBSCRIPTION WHERE USERID='" + userID + "'";
            //System.out.println(query);
            ResultSet rs = statement.executeQuery(query);
            subID = rs.getInt("ID");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        //System.out.println("Exiting Subscription ID");
        return subID;
    }
    public static int getPaymentID(String email){
        Connection connection = null;
        int paymentID = -1;
        int subID = Utilities.getSubscriptionID(email);
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT ID FROM PAYMENT WHERE SUBSID='" + subID + "'";
            ResultSet rs = statement.executeQuery(query);
            paymentID = rs.getInt("ID");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return paymentID;
    }
    public static Long getNextPaymentDate(String email){
        Connection connection = null;
        Long nextPaymentDate = null;
        int subID = Utilities.getSubscriptionID(email);
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT NEXTPAYMENTDATE FROM PAYMENT WHERE SUBSID='" + subID + "'";
            ResultSet rs = statement.executeQuery(query);
            nextPaymentDate= rs.getLong("NEXTPAYMENTDATE");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return nextPaymentDate;
    }
    protected static Payment getPayPalData(String email){
        Connection connection = null;
        String query;
        int subID = Utilities.getSubscriptionID(email);
        int paymentID = Utilities.getPaymentID(email);
        Payment payment = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String PayPalEmail;
            String PayPalPassword;
            Calendar calendar;

            query = "SELECT NEXTPAYMENTDATE FROM PAYMENT WHERE SUBID='" + subID + "'";
            ResultSet rs = statement.executeQuery(query);
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(rs.getInt("NEXTPAYMENTDATE"));
            rs.close();

            query = "SELECT * FROM PAYPAL WHERE PAYMENTID=" + paymentID + "'";
            PayPalEmail = rs.getString("EMAIL");
            PayPalPassword = rs.getString("PASSWORD");
            rs.close();

            payment = new Paypal(PayPalEmail, PayPalPassword);
            payment.setNextPaymentDate(calendar);
            while(rs.next()){
                System.out.println(rs.getInt("ID"));
            }
        }catch (SQLException e){
            System.err.println(e);
        }
        return payment;
    }
    public static int getPaymentMethod(String email){
        Connection connection = null;
        int paymentMethod = -1;
        int subID = Utilities.getSubscriptionID(email);
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT PAYMENTMETHOD FROM PAYMENT WHERE SUBSID='" + subID + "'";
            ResultSet rs = statement.executeQuery(query);
            paymentMethod = rs.getInt("PAYMENTMETHOD");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return paymentMethod;
    }
    protected static boolean checkIfStringIsEmpty(String s){
        return s.equals("") || s.length() < 1;
    }
    protected static ObservableList<Device> getDevices(){
        ObservableList<Device> devices = FXCollections.observableArrayList();
        int id = getUserID(Authenticator.getUser().getEmail());
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM DEVICES WHERE USERID='" + id + "'";
            ResultSet rs = statement.executeQuery(query);
            int counter = 1;
            while(rs.next()){
                Device device = new Device(rs.getString("NAME"), rs.getLong("ADDEDAT"), rs.getInt("TYPE"), counter);
                devices.add(device);
                //System.out.println(counter);
                counter++;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return devices;
    }
    protected static boolean removeDevice(String name){
        String singleQuery;
        singleQuery = "DELETE FROM DEVICES WHERE NAME='" + name + "'";
        executeSingleQuery(singleQuery);
        return true;
    }
    protected static boolean checkDevice(String name){
        String singleQuery;
        singleQuery = "SELECT EXISTS(SELECT * FROM DEVICES WHERE NAME='" + name + "' AND USERID=" +
                getUserID(Authenticator.getUser().getEmail())
                + ") AS TEST";
        Connection connection = null;
        boolean deviceExists = false;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery(singleQuery);
            //System.out.println(rs.getInt("TEST"));
            if(rs.getInt("TEST") == 1){
                deviceExists = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return deviceExists;
    }
    protected static long deviceAdded(String name){
        String singleQuery;
        singleQuery = "SELECT ADDEDAT AS DATE FROM DEVICES WHERE NAME='" + name + "'";
        Connection connection = null;
        Long addedAt = null;
        boolean deviceExists = false;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery(singleQuery);
            System.out.println(rs.getLong("DATE"));
            addedAt = rs.getLong("DATE");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return addedAt;
    }
    protected static int deviceCount(){
        String singleQuery;
        singleQuery = "SELECT COUNT(*) AS LIST FROM DEVICES WHERE USERID=" + getUserID(Authenticator.getUser().getEmail());
        Connection connection = null;
        int count = 0;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery(singleQuery);
            //System.out.println(rs.getInt("LIST"));
            count = rs.getInt("LIST");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return count;
    }
    public static String getUserName(int id){
        String name = "";
        String singleQuery;
        singleQuery = "SELECT * FROM USERDETAILS WHERE USERID='" + id + "'";
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery(singleQuery);
            name = rs.getString("FIRSTNAME") + " " + rs.getString("LASTNAME");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        System.out.println(name);
        return name;
    }
    public static boolean paymentExists(){
        String singleQuery = "SELECT COUNT(*) AS LIST FROM PAYMENT WHERE SUBSID=" +
                getSubscriptionID(Authenticator.getUser().getEmail());
        Connection connection = null;
        boolean exists = false;
        int count = 0;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery(singleQuery);
            count = rs.getInt("LIST");
            if(count != 0){
                exists = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                System.err.println(e);
            }
        }
        return exists;
    }
    public static boolean removePayment(){
        String singleQuery = "DELETE FROM PAYMENT WHERE SUBSID=" + getSubscriptionID(Authenticator.getUser().getEmail());
        executeSingleQuery(singleQuery);
        return true;
    }
    public static String escapeString(String string){
        string = string.replace("'", "");
        string = string.replace("\"", "");
        return string;
    }
}
