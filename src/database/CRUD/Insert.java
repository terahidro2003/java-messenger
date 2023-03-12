package database.CRUD;

import database.Table.Column;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;

public class Insert extends CRUDOperation{
    private String insertQuery;
    public Insert() throws Exception {
        super();
    }

    public Insert(String name) throws Exception {
        super(name);
    }

    public void insert(int rowIndex) throws Exception {
        //check if row with the same id exists
        System.out.println(this.Columns.get(0).getValue(0));
//        Column idCol = Columns.get(0);
//        Select select = new Select(name, this.Columns);
//        select.SelectByID(Integer.parseInt(idCol.getValue(rowIndex)));
//        if(select.Columns.get(0).values.size() > 0) return;


        //if no duplicate IDs exist, continue
        insertQuery = "INSERT into " + name + "(";
        insertQuery += helper.appendWithSeparator(getColumnNames(), ", ");
        insertQuery += ") VALUES (";
        String[] QuestionMarks = new String[getColumnNames().length];
        Arrays.fill(QuestionMarks, "?");
        insertQuery += helper.appendWithSeparator(QuestionMarks, ", ");
        insertQuery += ");";

        preparedStatement = connection.prepareStatement(insertQuery);

        int colIndex = 0;
        for(Column col : Columns)
        {
            System.out.println("DB Insert: inserting into " + col.getName() + "(" + col.getType() + ") value = " + col.getValue(rowIndex));
            if(col.getType() == "int") preparedStatement.setInt(colIndex+1, Integer.parseInt(col.getValue(rowIndex)));
            if(col.getType() == "text") preparedStatement.setString(colIndex+1, col.getValue(rowIndex));
            colIndex++;
        }
        try{
            preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e)
        {
            System.out.println("Duplicate entry detected! Skipping to insert this record");
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
            System.out.println(updateQuery);
            preparedStatement = connection.prepareStatement(updateQuery);
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
            //connection.close();
        }

    }

    public void InsertAllRows() throws Exception {
        for(int i = 0; i<rowCount; i++)
        {
            System.out.println("DB Insert: inserting " + i+1 + "-th row");
            this.insert(i);
        }
    }
}
