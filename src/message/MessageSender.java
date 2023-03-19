package message;

import database.DatabaseConnection;
import database.JDBCConnection;
import user.User;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class MessageSender implements Runnable{
    private Thread t;
    private String threadName;
    private User recipient;
    private User sender;

    private String latestContent;

    public MessageSender(User sender, User recipient)
    {
        Random rand = new Random();
        this.recipient = recipient;
        this.sender = sender;
        threadName = "MessageSenderThread-" + String.valueOf(rand.nextInt(1111,987777));
    }
    public void run() {
        this.awaitForInput();
    }

    private void awaitForInput()
    {
        try {
            while(true)
            {
                Scanner scanner = new Scanner(System.in);
                String messageContent = scanner.nextLine();
                latestContent = messageContent;
                if(messageContent != null || !messageContent.equals(""))
                {
                    Message message = new Message(messageContent, sender, recipient.Columns.get(1).values.get(0));
                    message.send();
                    message.disconnect();
                }
                JDBCConnection.connection.close();
                Thread.sleep(500);
            }
        } catch (SQLException e1){
            System.out.println(e1);
            System.out.println("Trying to resend message");
            try{
                JDBCConnection.connect();
                Message message = new Message(latestContent, sender, recipient.Columns.get(1).values.get(0));
                message.send();
                message.disconnect();
            } catch (Exception e) {
                System.out.println("ERROR: Trying to resend message has failed!");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void start()
    {
        if(t == null)
        {
            t = new Thread(this, threadName);
            t.start();
        }
    }

//    public void finalize(){
//        try{
//            JDBCConnection.connection.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
