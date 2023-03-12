package database.CRUD;

import java.sql.SQLException;

public class Delete extends CRUDOperation{

    private String deleteQuery;
    Delete() throws Exception
    {
        super();
    }

    Delete(String name) throws Exception{
        super(name);
    }

    public void Delete(int id) throws SQLException {
        deleteQuery = "DELETE from " + name + " where id=" + id + ";";
        preparedStatement = connection.prepareStatement(deleteQuery);
        try{
            preparedStatement.execute();
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
