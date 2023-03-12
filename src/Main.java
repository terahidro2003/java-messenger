import database.CRUD.CRUDOperation;
import database.CRUD.Select;
import database.DatabaseConnection;
import database.Table.Table;

import java.sql.SQLException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Messenger App. v0.0.1");
        Table usersTable = new Table("users");
        usersTable.addColumn("id", "int");
        usersTable.addColumn("username", "text");
        usersTable.addColumn("password", "text");
        String values[] = new String[3];
        values[0] = "4";
        values[1] = "Ula";
        values[2] = "1234";
        usersTable.addRow(values);

        values[0] = "6";
        values[1] = "Ona";
        values[2] = "384hfdhf";
        usersTable.addRow(values);

        values[0] = "5";
        values[1] = "Jonas";
        values[2] = "juudshds1";
        usersTable.addRow(values);

        usersTable.print();

        //CREATE Operations (INSERT)
        usersTable.save();


        //READ Operations (SELECT)
        usersTable.selectAll();
        usersTable.print();
//        usersTable.selectId(2);
//        usersTable.print();
//        usersTable.select("username = 'Juozas'");
//        usersTable.print();
    }
}