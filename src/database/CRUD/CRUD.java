package database.CRUD;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CRUD {
    private boolean checkIfTableExists() throws SQLException {
       return false;
    }

}
