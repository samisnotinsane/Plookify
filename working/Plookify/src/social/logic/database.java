package social.logic;

//import account

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;


/**
 * Created by Mohammad Nawaz on 12/02/2016.
 */
public class database {

    public static ObservableList<String> friends = FXCollections.observableArrayList();
    public static ObservableList<String> FRequests = FXCollections.observableArrayList();
    public static ObservableList<String> playlists = FXCollections.observableArrayList();
    public static ObservableList<String> tracks;


    int dbCuser = CUser.getCuser(); // to get the current user

    Connection con = null;

    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");

            con = DriverManager.getConnection("jdbc:sqlite:bin/test.db");


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // System.out.println(ex.getMessage());
            e.printStackTrace();
            System.out.println("db didnt work");

        }
        return con;
    }


    public void testdb() {
        try {
            Statement stmt = con.createStatement();


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

    }

    // to get user's friends
    public ObservableList<String> Ufriends() {
        friends.clear();
        ArrayList<String> tempFID = new ArrayList<>();


        try {
            con = getConnection();
            Statement stmt = con.createStatement();


            ResultSet rs = stmt.executeQuery("SELECT FIRSTNAME, LASTNAME, USERID  FROM USERDETAILS WHERE USERID IN(SELECT friend_id from friendstbl WHERE user_id = '" + dbCuser + "');");

            int temp = 0;

            while (rs.next()) {


                String TFname = rs.getString("FIRSTNAME");
                String TSname = rs.getString("LASTNAME");


                temp = rs.getInt("USERID");

                friends.add(temp + " " + TFname + " " + TSname);


            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;

    }


    public static void main(String[] args) {

        database db = new database();
        Connection b = db.getConnection();

    }


    public void notprivmthd1() {


        ResultSet rs = null;

        Statement stmt;

        try {
            con = getConnection();
            stmt = con.createStatement();// stmtm comment
            stmt.executeUpdate("UPDATE disctbl SET disc = 1  WHERE user_id =  '" + dbCuser + "' ");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void privmthd1() {


        ResultSet rs = null;

        Statement stmt;

        try {
            con = getConnection();
            stmt = con.createStatement();// stmtm comment
            stmt.executeUpdate("UPDATE disctbl SET disc = 0  WHERE user_id =  '" + dbCuser + "' ");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ObservableList<String> Fplaylist(int Fid) {
        playlists.clear();


        ResultSet rs = null;
        PreparedStatement pst = null;


        try {

            con = getConnection();
            Statement stmt = con.createStatement();


            rs = stmt.executeQuery("SELECT id, name FROM PLAYLIST WHERE addedBy= '" + Fid + "' AND type = 'friend' ");
            while (rs.next()) {
                int P_id = rs.getInt("id");
                String TFname = rs.getString("name");


                playlists.add(P_id + " " + TFname);


            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }


        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists;
    }


    public ObservableList<String> Ftracks(int Pid) {


        tracks = FXCollections.observableArrayList();
        ResultSet rs = null;
        PreparedStatement pst = null;


        try {

            con = getConnection();
            Statement stmt = con.createStatement();


            rs = stmt.executeQuery("SELECT NAME FROM TRACKS WHERE ID IN (SELECT trackId from PLAYLISTTRACK WHERE playlistId= '" + Pid + "');");
            while (rs.next()) {

                String TFname = rs.getString("NAME");


                tracks.add(TFname);


            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }


        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tracks;
    }


    // to check friend requests for a user
    public ObservableList<String> Frequests() {
        FRequests.clear();

        ResultSet rs = null;

        Statement stmt;

        try {
            con = getConnection();
            stmt = con.createStatement();


            rs = stmt.executeQuery("SELECT FIRSTNAME, LASTNAME, USERID  FROM USERDETAILS WHERE USERID IN (SELECT user_id from requesttbl WHERE friend_id= '" + dbCuser + "' AND status = 'pending');");

            int temp = 0;

            while (rs.next()) {


                String TFname = rs.getString("FIRSTNAME");
                String TSname = rs.getString("LASTNAME");


                temp = rs.getInt("USERID");

                FRequests.add(temp + " " + TFname + " " + TSname);


            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FRequests;
    }


    public String getmsg(int Fid) {

        String msg = "";

        ResultSet rs = null;

        Statement stmt;

        try {
            con = getConnection();
            stmt = con.createStatement();


            rs = stmt.executeQuery("SELECT msg FROM requesttbl WHERE friend_id  = '" + dbCuser + "' AND user_id  = '" + Fid + "' ");


            msg = rs.getString("msg");


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }


        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return msg;
    }

    // addfriend  method to add a friend
    public void addfriend(int Reqid) {


        ResultSet rs = null;
        Statement stmt;

        try {
            con = getConnection();
            stmt = con.createStatement();

            stmt.executeUpdate("INSERT INTO friendstbl VALUES( NULL, '" + dbCuser + "' , '" + Reqid + "');");

            stmt.executeUpdate("DELETE FROM requesttbl WHERE user_id= '" + Reqid + "' AND friend_id = '" + dbCuser + "' ");


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void reject(int Reqid) {


        ResultSet rs = null;
        Statement stmt;

        try {
            con = getConnection();
            stmt = con.createStatement();

            stmt.executeUpdate("DELETE FROM REQUESTTBL WHERE user_id= '" + Reqid + "' AND friend_id = '" + dbCuser + "' ");


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //remove friend method......
    public void removefriend(int Fid) {


        ResultSet rs = null;

        Statement stmt;


        try {
            con = getConnection();
            stmt = con.createStatement();// stmtm comment
            stmt.executeUpdate("DELETE FROM friendstbl WHERE user_id= '" + dbCuser + "' AND friend_id = '" + Fid + "';");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getfriendid(String name) {
        ResultSet rs = null;
        PreparedStatement pst = null;


        int id = 0;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT USERID FROM USERDETAILS WHERE FIRSTNAME= '" + name + "' ");
            try {
                id = rs.getInt("USERID");
            } catch (Exception e) {
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int getid(String email) {


        ResultSet rs = null;
        PreparedStatement pst = null;


        int id = 0;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT ID FROM USER WHERE EMAIL= '" + email + "' ");
            try {
                id = rs.getInt("ID");
                // System.out.println(disc + " disc 2222 ID ID ID");
            } catch (Exception e) {
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName

        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;


    }


    public void sendrequest(String Femail, String msg) {

        ResultSet rs = null;
        PreparedStatement pst = null;


        try {

            int getid = getid(Femail);
            con = getConnection();
            Statement stmt = con.createStatement();


            stmt.executeUpdate("INSERT INTO requesttbl VALUES( NULL, '" + dbCuser + "' , '" + getid + "', '" + msg + "','pending');");


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public int isdisc(String email) {
        ResultSet rs = null;
        PreparedStatement pst = null;


        int getid = getid(email);

        System.out.println(getid + " 2222 ID ID ID");
        int disc = 0;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT disc FROM disctbl WHERE user_id= '" + getid + "' ");
            try {
                disc = rs.getInt("disc");

            } catch (Exception e) {
                disc = 0;
            }


            stmt.close();

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            // e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disc;
    }


    public int ischecked() {
        ResultSet rs = null;
        PreparedStatement pst = null;


        int disc = 0;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT disc FROM disctbl WHERE user_id= '" + dbCuser + "' ");

            disc = rs.getInt("disc");


            stmt.close();

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName

        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disc;
    }


    public int checkPaid(String email) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        int ifpaid = 0;
        int id = getid(email);

        try {
            con  = getConnection();
            Statement stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT SUBSTYPE FROM USERSUBSCRIPTION WHERE USERID= '" + id + "' ");
            try {
                ifpaid = rs.getInt("SUBSTYPE");
            } catch (Exception e) {
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ifpaid;
    }


    public int sentfriends(String email) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        int yes = 0;
        int exist = -2;
        int id = getid(email);

        try {
            con = getConnection();
            Statement stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT request_id FROM requesttbl WHERE user_id  = '" + dbCuser + "' AND friend_id  = '" + id + "' AND status= 'pending' ");
            try {
                exist = rs.getInt("request_id");

            } catch (Exception e) {
            }
            if (exist != -2 && exist != 65) {
                yes = 1;
            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return yes;
    }


    public int alreadyfriends(String email) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        int yes = 0;
        int exist = -2;
        int id = getid(email);

        try {
            con = getConnection();
            Statement stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT friendship_id FROM FRIENDSTBL WHERE user_id  = '" + dbCuser + "' AND friend_id  = '" + id + "' ");
            try {
                exist = rs.getInt("friendship_id");

            } catch (Exception e) {
            }
            if (exist != -2 && exist != 65) {
                yes = 1;
            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return yes;
    }

    public int ownUser(String email) {

        int yes = 0;

        int id = getid(email);

        if (dbCuser == id)
            yes = 1;


        return yes;
    }


}
