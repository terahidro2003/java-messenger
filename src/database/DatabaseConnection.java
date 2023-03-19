package database;

import database.CRUD.CRUDOperation;
import database.Table.Table;

import javax.xml.crypto.Data;

import java.sql.*;

public class DatabaseConnection {

    //public static Connection connection;
    public Statement statement;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;

    public DatabaseConnection() throws Exception {
        JDBCConnection.connect();
        statement = JDBCConnection.connection.createStatement();
    }

    //disconnect from database
    public void disconnect() throws Exception{
        try{
            if(statement != null) statement.close();
            if(preparedStatement != null) preparedStatement.close();
            if(resultSet != null) resultSet.close();
            JDBCConnection.connection.close();
        }catch (Exception e)
        {
            System.out.println("Failed to close connection!");
            throw e;
        }
    }
}
