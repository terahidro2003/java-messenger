package database.Table;

import database.DatabaseConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Table extends DatabaseConnection {
    public List<Column> Columns = new ArrayList<>();
    public String name;
    public int rowCount = 0;

    public Table() throws Exception {
        super();
        System.out.println("Please provide table name with getters and setters.");
        this.name = "unidentifiedTable";
    }

    public Table(String name) throws Exception
    {
        super();
        this.name = name;
    }

    public void addColumn(String name, String type)
    {

        boolean isUnique = true;
        //check if new column is unique
        if(columnCount() > 0)
        {
            for(Column col : Columns)
            {
                if(name.equals(col.getName()) && type.equals(col.getType()))
                {
                    isUnique = false;
                }
            }
        }
        if(isUnique)
        {
            Column newColumn = new Column(type, name);
            Columns.add(newColumn);
        }else{
            System.out.println("Such column already exists!");
        }

    }

    public int columnCount()
    {
        int count = 0;
        if(Columns.size() > 0) for(Column col : Columns) count++;
        return count;
    }

    public void addRow(String[] values)
    {
        if(values.length == columnCount())
        {
            for(int i = 0; i<values.length; i++)
            {
                Columns.get(i).values.add(values[i]);
            }
            rowCount++;
        }else{
            return;
        }
    }

    public String[] getNames()
    {
        int itterator = 0;
        String[] columnNames;
        columnNames = new String[columnCount()];
        for(Column col : Columns)
        {
            columnNames[itterator] = col.getName();
            itterator++;
        }
        return columnNames;
    }

    public void print()
    {
        System.out.println("Table:" + name);
        for(Column col : Columns)
        {
            System.out.print(col.getName() + " | ");
        }
        System.out.println();
        for(int i = 0; i<rowCount; i++)
        {
            HashMap<String, String> printableRows = getRow(i);
            for(Column col : Columns)
            {
                System.out.print(printableRows.get(col.getName()) + " | ");
            }
            System.out.println();
        }

    }

    public Column search(String name)
    {
        for(Column col : Columns)
        {
            if(name.equals(col.getName()))
            {
                return col;
            }
        }
        return null;
    }

    public HashMap<String, String> getRow(int index)
    {
        HashMap<String, String> row = new HashMap<>();
        for(Column col : Columns)
        {
            row.put(col.getName(), col.getValue(index));
            //System.out.println(col.getName() + " " + col.getValue(index));
        }
        return row;
    }

    public String[] getColumnNames()
    {
        int itterator = 0;
        String[] columnNames;
        columnNames = new String[columnCount()];
        for(Column col : Columns)
        {
            columnNames[itterator] = col.getName();
            itterator++;
        }
        return columnNames;
    }

    public void resetValues(Boolean table)
    {
        for(Column col : Columns)
        {
            col.resetValues();
        }
        rowCount = 0;
    }

//    private void mapDBtoTable(@NotNull CRUDOperation op)
//    {
//        this.Columns = op.Columns;
//        this.rowCount = op.rowCount;
//    }

//    private Insert insertCRUDop() throws Exception{
//        Insert insert = new Insert(name);
//        insert.setTable(this);
//        return insert;
//    }

//    public void insert(int rowIndex) throws Exception
//    {
//        Insert insert = insertCRUDop();
//        insert.insert(rowIndex);
//    }

//    public void save() throws Exception
//    {
//        Insert insert = insertCRUDop();
//        insert.InsertAllRows();
//    }
//
//    private Select selectCRUDOp() throws Exception {
//        Select select = new Select(name);
//        select.setTable(this);
//        return select;
//    }
//
//    public void selectAll() throws Exception {
//        Select select = selectCRUDOp();
//        select.SelectAll();
//        mapDBtoTable(select);
//    }
//
//    public void select(String condition) throws Exception
//    {
//        Select select = selectCRUDOp();
//        select.Select(condition);
//        mapDBtoTable(select);
//    }
//
//    public void selectId(int id) throws Exception{
//        Select select = selectCRUDOp();
//        select.SelectByID(id);
//        mapDBtoTable(select);
//    }
}
