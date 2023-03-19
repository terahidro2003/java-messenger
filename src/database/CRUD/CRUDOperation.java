package database.CRUD;

import database.DatabaseConnection;
import database.JDBCConnection;
import database.Table.Column;
import database.Table.Table;
import helpers.Helper;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.*;

public class CRUDOperation extends Table implements CRUD {
    protected Helper helper = new Helper();
    private String insertQuery;
    private String selectQuery;
    private String deleteQuery;


    public CRUDOperation() throws Exception {
        super();
    }

    public CRUDOperation(String name) throws Exception{
        super(name);
    }

    //checks if table exists in DB
    protected void checkIfTableExists() throws SQLException {
        statement = JDBCConnection.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = statement.executeQuery("show full tables where Table_Type LIKE 'BASE TABLE' AND Tables_in_" + JDBCConnection.database_name + " LIKE '" + name + "';");
        if(!rs.last()) {
            System.out.println("WARNING: table" + name + " does not exist");
            this.initializeTable();
            //return false;
        }
    }

    //if table doesn't exist, creates missing table in DB
    private void initializeTable() throws SQLException {
        String createTableQuery = "CREATE TABLE " + super.name + "( ";

        String[] arr = new String[Columns.size()];
        int index = 0;
        for(Column col : Columns)
        {
            String text = col.getName() + " ";
            if(col.getType().equals("int"))
            {
                text += "INT";
            }else{
                text += "varchar(255)";
            }

            if(col.getName() == "id")
            {
                text += " primary key auto_increment";
            }

            arr[index] = text;
            index++;
        }

        createTableQuery += helper.appendWithSeparator(arr, ", ") + ");";
        preparedStatement = JDBCConnection.connection.prepareStatement(createTableQuery);
        preparedStatement.executeUpdate();
        System.out.println(createTableQuery);
    }

    public void setTable(Table table)
    {
        super.name = table.name;
        super.Columns = table.Columns;
        super.rowCount = table.rowCount;
    }

    /*
    -----------------------------
    CRUD Operation Methods
    -----------------------------
     */

    public void insert(int rowIndex) throws Exception {

        if(JDBCConnection.connection.isClosed())
        {
            JDBCConnection.connect();
        }
        if(statement.isClosed())
        {
            statement = JDBCConnection.connection.createStatement();
        }
        checkIfTableExists();
        //if no duplicate IDs exist, continue
        insertQuery = "INSERT into " + name + "(";
        insertQuery += helper.appendWithSeparator(getColumnNames(), ", ");
        insertQuery += ") VALUES (";
        String[] QuestionMarks = new String[getColumnNames().length];
        Arrays.fill(QuestionMarks, "?");
        insertQuery += helper.appendWithSeparator(QuestionMarks, ", ");
        insertQuery += ");";

        preparedStatement = JDBCConnection.connection.prepareStatement(insertQuery);

        int colIndex = 0;
        for(Column col : Columns)
        {
            //System.out.println("DB Insert: inserting into " + col.getName() + "(" + col.getType() + ") value = " + col.getValue(rowIndex));
            if(col.getType() == "int") preparedStatement.setInt(colIndex+1, Integer.parseInt(col.getValue(rowIndex)));
            if(col.getType() == "text") preparedStatement.setString(colIndex+1, col.getValue(rowIndex));
            colIndex++;
        }
        try{
            preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e)
        {
            //System.out.println("Duplicate entry detected! Skipping to insert this record");
            String updateQuery = "UPDATE " + name + " SET ";
            String[] updatableColumnsConditions = new String[getColumnNames().length];
            int i = 0;
            for(Column col : Columns)
            {
                if(col.getType() == "int")
                {
                    updatableColumnsConditions[i] = col.getName() + " = " + col.values.get(rowIndex);
                }else{
                    updatableColumnsConditions[i] = col.getName() + " = '" + col.values.get(rowIndex) + "'";
                }
                i++;
            }
            updateQuery += helper.appendWithSeparator(updatableColumnsConditions, ", ");
            updateQuery += " WHERE id= " + Columns.get(0).values.get(0) + ";";
            // System.out.println(updateQuery);
            preparedStatement = JDBCConnection.connection.prepareStatement(updateQuery);
            try{
                preparedStatement.execute();
            }catch (SQLException ee)
            {
                System.out.println(ee.toString());
            }
        }
        catch (SQLException e)
        {
            System.out.println("ERROR: check your query");
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        finally {
            disconnect();
        }

    }

    public void InsertAllRows() throws Exception {
        for(int i = 0; i<rowCount; i++)
        {
            //System.out.println("DB Insert: inserting " + i+1 + "-th row");
            this.insert(i);
        }
    }

    private void selectOperation() throws Exception {

        if(JDBCConnection.connection.isClosed())
        {
            JDBCConnection.connect();
        }
        if(statement.isClosed())
        {
            statement = JDBCConnection.connection.createStatement();
        }
        checkIfTableExists();

        ResultSet resultset = statement.executeQuery(selectQuery);

        //Clean previous values from each column
        resetValues(true);

        //Add values to the Columns object
        while(resultset.next())
        {
            for(Column col : Columns)
            {
                //System.out.println("DB Select: Selecting [" + col.getName() + "] = '" + resultset.getString(col.getName()) + "'");
                col.values.add(resultset.getString(col.getName()));
            }
            this.rowCount++;
        }
        disconnect();
    }

    public void SelectAll() throws Exception
    {
        selectQuery = "SELECT * FROM " + name + ";";
        statement = JDBCConnection.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        this.selectOperation();
    }

    public void SelectByID(int id) throws Exception
    {
        selectQuery = "SELECT * FROM " + name + " WHERE id=" + id + ";";
        this.selectOperation();
        if(rowCount > 1)
        {
            //System.out.println("[CRITICAL] DB: Duplicate ID found");
        }
    }

    public void Select(String whereCondition) throws Exception {
        selectQuery = "SELECT * FROM " + name + " WHERE " + whereCondition;
        //System.out.println(selectQuery);
        this.selectOperation();
    }

    public void Delete(int id) throws SQLException {
        checkIfTableExists();
        deleteQuery = "DELETE from " + name + " where id=" + id + ";";
        preparedStatement = JDBCConnection.connection.prepareStatement(deleteQuery);
        try{
            preparedStatement.execute();
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public int generateID() throws Exception {
        if(JDBCConnection.connection.isClosed())
        {
            JDBCConnection.connect();
        }
        if(statement.isClosed())
        {
            statement = JDBCConnection.connection.createStatement();
        }
        checkIfTableExists();
        Random ran = new Random();
        int id = 0;
        SelectByID(id);
        while(Columns.get(0).values.size() != 0)
        {
            id = ran.nextInt(11, 999999);
            this.SelectByID(id);
        }
        this.resetValues(true);
        return id;
    }

}
