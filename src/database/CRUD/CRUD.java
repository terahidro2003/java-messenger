package database.CRUD;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CRUD {
    default boolean checkIfTableExists() throws SQLException {
       return false;
    }

}
