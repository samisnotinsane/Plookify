package player;

import java.sql.*;

public class DBtest
{

    Connection c = null;
    Statement stmt = null;
    ResultSet rs = null;

    public DBtest(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
            c.setAutoCommit(false);
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public boolean insert(String sql){
        try{
        stmt = c.createStatement();
        stmt.execute(sql);
        stmt.close();
        c.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(String sql){
        int rows;
        try{
            stmt = c.createStatement();
            rows = stmt.executeUpdate(sql);
            stmt.close();
            c.commit();

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        if(rows == 0){
            return false;
        }
        return true;
    }

    public ResultSet select(String sql){
        try{
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
        }
        catch(Exception e){
            e.printStackTrace();
            return rs;
        }

        return rs;
    }

    public boolean close() throws SQLException {
        c.close();
        return true;
    }

    public boolean close2() throws SQLException {
        stmt.close();
        c.close();
        return true;
    }

}