package user;

import database.CRUD.CRUDOperation;
import database.Table.Column;
import database.Table.Table;

public class User extends CRUDOperation {
    private int id;
    private String username;
    private String password;
    private boolean isLoggedIn = false;

    User() throws Exception {
        super("users");
        this.addColumns();
        System.out.println("Please initialize this object using user's credentials");
    }

    //Constructor for login process
    public User(String username, String password) throws Exception {
        super("users");
        this.addColumns();
        super.Select("username = '" + username + "' AND password = '" + password + "'");
        if(Columns.get(0).values.size() != 1)
        {
            System.out.println("User not found. Please signup or try again");
            super.resetValues(true); //reset user obj
        }else{
            isLoggedIn = true;
        }
    }

    //gets user from DB by id
    public User(int id) throws Exception
    {
        super("users");
        this.addColumns();
        this.Select("id = " + id + " limit 1");
        if(Columns.get(0).values.size() == 0) {
            System.out.println("Such user does not exist");
            this.resetValues(true);
        }
    }

    //not designed to authenticate but store other user's data as object
    public User(String username) throws Exception {
        super("users");
        this.username = username;
        this.password = null;
        this.addColumns();
        this.Select("username = '" + username + "'");
        if(Columns.get(0).values.size() == 0)
        {
            System.out.println("Such user does not exist");
            this.resetValues(true);
        }else if(Columns.get(0).values.size() > 1)
        {
            System.out.println("Multiple users with same username exist. Picking the first in DB");
            resetValues(true);
            this.Select("username = '" + username + "' LIMIT 1");
        }
    }

    public boolean loggedIn()
    {
        return isLoggedIn;
    }

    //initiates columns
    private void addColumns()
    {
        super.Columns.add(new Column("int", "id", true));
        super.Columns.add(new Column("text", "username", true));
        super.Columns.add(new Column("text", "password", true));
    }

    public void Signup(String username, String password) throws Exception {
        System.out.println("Signing up a user...");
        String values[] = new String[3];
        values[0] = String.valueOf(generateID());
        values[1] = username;
        values[2] = password;
        super.addRow(values);
        super.insert(0);
        this.username = username;
        this.password = password;
        this.id = Integer.parseInt(values[0]);
//        System.out.println("User is logged in");
//        this.isLoggedIn = true;
    }
}
