package database.Table;

import java.util.ArrayList;
import java.util.List;

public class Column{
    private String Type;
    private String Name;
    public boolean required = false;
    //private int index;
    public List<String> values = new ArrayList<>(); //keeps values of that column

    public boolean isRequired()
    {
        return required;
    }
    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Column()
    {
        Type = "text"; //default column type
    }

    public String getValue(int index)
    {
        for(int i = 0; i<values.size(); i++)
        {
            if(i == index) return values.get(i);
        }
        return null;
    }



    public Column(String t, String n)
    {
        //verify types in the future!
        Type = t;
        Name = n;
    }

    public Column(String t, String n, boolean required)
    {
        Type = t;
        Name = n;
        this.required = required;
    }

    // Deletes all column's values
    public void resetValues()
    {
        this.values = new ArrayList<>();
    }
}
