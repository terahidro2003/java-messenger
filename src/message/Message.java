package message;

import database.CRUD.CRUDOperation;
import database.Table.Column;
import database.Table.Table;
import user.User;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Message extends CRUDOperation {
    private int id = -1;
    private String content;
    private User sender;
    private User recipient;
    private String timeSent;

    public Message(String content, User sender, String recipientUsername) throws Exception {
        super("messages");

        //add columns of table
        super.addColumn("id", "int");
        super.addColumn("content", "text");
        super.addColumn("sender", "int");
        super.addColumn("recipient", "int");
        super.addColumn("time", "text");
        super.addColumn("r", "text");

        this.content = content;
        this.sender = sender;
        this.recipient = new User(recipientUsername);

        //Set values of object
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        this.timeSent = timestamp.toString();
        String values[] = new String[6];
        values[0] = String.valueOf(generateID());
        values[1] = this.content;
        values[2] = this.sender.Columns.get(0).getValue(0);
        values[3] = this.recipient.Columns.get(0).getValue(0);
        values[4] = this.timeSent;
        values[5] = "false";

        //Add new entry
        super.addRow(values);
    }

    public Message(User current) throws Exception {
        super("messages");
        super.addColumn("id", "int");
        super.addColumn("content", "text");
        super.addColumn("sender", "int");
        super.addColumn("recipient", "int");
        super.addColumn("time", "text");
        super.addColumn("r", "text");
        super.Select("sender = " + current.Columns.get(0).values.get(0) + " AND r='false' ORDER BY time DESC");
    }

    //Getter and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    //Sends a message (actually saves it do DB)
    public void send() throws Exception {
        InsertAllRows();
    }

}
