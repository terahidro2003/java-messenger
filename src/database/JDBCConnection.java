package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
Abstract Class for JDBC db connection
 */
public abstract class JDBCConnection {
    //DB details:
    public static Connection connection;
    private final static String hostname = "localhost";
    public final static String database_name = "messenger_test";
    private final static String database_user = "lmas";
    private final static String database_pwd = "lmas";


    //Connects to the database (static function to keep connections amount minimum)
    public static void connect() throws Exception
    {
        String connectionString = "jdbc:mysql://"+hostname+"/"+database_name+"?user=" + database_user
                + "&password=" + database_pwd;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString);
            //System.out.println("Data Controller: successfully connected to DB (" + database_name + ")");
        }catch (SQLException e)
        {
            System.out.println("Connection to " + database_name + " failed");
            throw e;
        }
    }


}
