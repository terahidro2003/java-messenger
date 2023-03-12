package helpers;

public class Helper {
    public String appendWithSeparator(String[] appendees, String separator)
    {
        //checks, if collection is empty
        if(appendees[0] == null) return null;

        //appends separators to each member of String collection
        String result = "";
        for(String a : appendees)
        {
            result += a;
            result += separator;
        }

        //returns a string with last separator removed from the end
        return result.length() > 0 ? result.substring(0, result.length() - separator.length()): "";
    }
}
