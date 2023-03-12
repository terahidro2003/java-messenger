package database.CRUD;

import database.DatabaseConnection;
import database.Table.Column;
import database.Table.Table;
import helpers.Helper;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class CRUDOperation extends DatabaseConnection implements CRUD {
    //protected String table_name;
    //protected Table table;
    protected Helper helper = new Helper();
    public CRUDOperation() throws Exception {
        super();
        if(!checkIfTableExists())
        {
            System.out.println("WARNING: table" + name + " does not exist");
        }
    }

    public CRUDOperation(String name) throws Exception{
        super(name);
        if(!checkIfTableExists())
        {
            System.out.println("WARNING: table" + name + " does not exist");
        }
    }

    public boolean checkIfTableExists() throws SQLException {
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = statement.executeQuery("show full tables where Table_Type LIKE 'BASE TABLE' AND Tables_in_" + DatabaseConnection.database_name + " LIKE '" + name + "';");
        if(!rs.last()) {
            return false;
        } else return true;
    }

    public void setTable(Table table)
    {
        super.name = table.name;
        super.Columns = table.Columns;
        super.rowCount = table.rowCount;
    }
}
