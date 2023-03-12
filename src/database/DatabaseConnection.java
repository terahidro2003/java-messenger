package database;

import database.Table.Table;

import javax.xml.crypto.Data;
import java.sql.*;

public class DatabaseConnection extends Table {
    private final static String hostname = "localhost";
    public final static String database_name = "messenger_test";
    private final static String database_user = "lmas";
    private final static String database_pwd = "lmas";

    public Connection connection;
    public Statement statement;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;

    public DatabaseConnection() throws Exception {
        connect();
    }

    public DatabaseConnection(String name) throws Exception{
        super(name);
        connect();
    }

    public void connect() throws Exception
    {
        String connectionString = "jdbc:mysql://"+hostname+"/"+database_name+"?user=" + database_user
                + "&password=" + database_pwd;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString);
            System.out.println("Data Controller: successfully connected to DB (" + database_name + ")");
        }catch (SQLException e)
        {
            System.out.println("Connection to " + database_name + " failed");
            throw e;
        }
    }

    public void disconnect() throws Exception{
        try{
            statement.close();
            preparedStatement.close();
            resultSet.close();
            connection.close();
        }catch (Exception e)
        {
            System.out.println("Failed to close connection!");
            throw e;
        }
    }
}
