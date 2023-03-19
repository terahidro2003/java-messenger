package message;

import GUI.MainWindow;
import database.DatabaseConnection;
import database.JDBCConnection;
import user.User;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class MessagesListener implements Runnable{
    private Thread t;
    private String threadName;
    private User user;
    private Message lastMessage;
    public boolean read = false;

    private ListModel<String> msgList;

    public MessagesListener(User user, ListModel<String> msgList){
        this.user = user;
        this.msgList = msgList;
        Random rand = new Random();
        threadName = "MessageListenerThread-" + String.valueOf(rand.nextInt(1111,987777));
    }

    public void run() {
        try{
            while(true)
            {
                Message message = new Message(user);
                if(message.Columns.get(0).values.size() > 0)
                {
                    List<String> contents = message.Columns.get(1).values;
                    for(String content : contents)
                    {
                        System.out.println(content);
                    }
                    int index = 0;
                    for(String readStatus : message.Columns.get(5).values)
                    {
                        if(readStatus.equals("false"))
                        {
                            message.Columns.get(5).values.set(index, "true");
                        }
                        index++;
                    }
                    message.InsertAllRows();
                }
                message.disconnect();
                Thread.sleep(2000);
            }
        }catch (InterruptedException e)
        {
            System.out.println(e.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                JDBCConnection.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start(){
        if(t == null)
        {
            t = new Thread(this, threadName);
            t.start();
        }
    }

//    public void finalize(){
//        try{
//            DatabaseConnection.connection.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
