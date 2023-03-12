package database.CRUD;

import database.Table.Column;
import database.Table.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Select extends CRUDOperation{
    private HashMap<String, String> selectableFields;
    private String selectQuery;

    public Select(HashMap<String, String> selectableFields) throws Exception {
        super();
        this.selectableFields = selectableFields;
//        if(selectableFields.size() == 0)
//        {
//            this.SelectAll();
//        }
        if(super.checkIfTableExists())
        {
           System.out.println("Dont know what do to yet");
        }
    }

    public Select(String name) throws Exception {
        super(name);
    }

    public Select(String name, List<Column> columns) throws Exception
    {
        super(name);
        this.Columns = columns;
    }

    public Select() throws Exception {
        super();
    }

    private void selectOperation() throws SQLException {
        ResultSet resultset = statement.executeQuery(selectQuery);

        //Clean previous values from each column
        resetValues(true);

        //Add values to the Columns object
        while(resultset.next())
        {
            for(Column col : Columns)
            {
                System.out.println("DB Select: Selecting [" + col.getName() + "] = '" + resultset.getString(col.getName()) + "'");
                col.values.add(resultset.getString(col.getName()));
            }
            this.rowCount++;
        }
    }

    public void SelectAll() throws SQLException
    {
        selectQuery = "SELECT * FROM " + name + ";";
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        this.selectOperation();
    }

    public void SelectByID(int id) throws SQLException
    {
        selectQuery = "SELECT * FROM " + name + " WHERE id=" + id + ";";
        this.selectOperation();
        if(rowCount > 1)
        {
            System.out.println("[CRITICAL] DB: Duplicate ID found");
        }
    }

    public void Select(String whereCondition) throws SQLException {
        selectQuery = "SELECT * FROM " + name + " WHERE " + whereCondition;
        this.selectOperation();
    }

}
