/*
 * Created by JFormDesigner on Wed Mar 15 12:48:04 CET 2023
 */

package GUI;

import java.awt.*;
import database.CRUD.CRUDOperation;
import database.JDBCConnection;
import message.Message;
import message.MessagesListener;
import user.User;

import javax.swing.*;
import javax.swing.GroupLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Juozas Skarbalius
 * @email  j.skarbalius@lancaster.ac.uk
 * Software Development (Java Programming Challenge)
 */
public class MainWindow extends JFrame {

    //Main window constructor
    public MainWindow(User user) throws Exception {
        initComponents(user);
    }

    //Initializes JavaSwing components together with their event listeners
    private void initComponents(User current) throws Exception {
        scrollPane1 = new JScrollPane();
        messagesArea = new JTextArea();
        textField1 = new JTextField();
        sendMessageBtn = new JButton();
        label1 = new JLabel();
        usernameLabel = new JLabel();
        logoutBtn = new JButton();
        label2 = new JLabel();
        usersBox = new JComboBox();

        //======== this ========
        setTitle("Messenger App");
        setType(Window.Type.UTILITY);
        setResizable(false);
        var contentPane = getContentPane();

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(messagesArea);
        }

        //---- sendMessageBtn ----
        sendMessageBtn.setText("Send");

        //---- label1 ----
        label1.setText("Logged in as:");

        //---- usernameLabel ----
        usernameLabel.setText("[Username]");

        //---- logoutBtn ----
        logoutBtn.setText("Logout");

        //---- label2 ----
        label2.setText("Chat with:");

        //---- Group Layout Setup ----
        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(label1)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(usernameLabel)
                            .addGap(18, 18, 18)
                            .addComponent(label2)
                            .addGap(18, 18, 18)
                            .addComponent(usersBox, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(logoutBtn))
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 561, GroupLayout.PREFERRED_SIZE)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 461, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(sendMessageBtn, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(42, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label1)
                        .addComponent(usernameLabel)
                        .addComponent(logoutBtn)
                        .addComponent(label2)
                        .addComponent(usersBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(sendMessageBtn))
                    .addContainerGap(9, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());

        //Set user label to username
        usernameLabel.setText(current.Columns.get(1).values.get(0));

        //Get all users except one that is currently logged in
        CRUDOperation table = new CRUDOperation("users");
        table.addColumn("id", "int");
        table.addColumn("username", "text");
        table.addColumn("password", "text");
        table.Select("NOT username = '" + current.Columns.get(1).values.get(0) + "'");

        //Keeps usernames of selected users and their respective id's
        HashMap<String, Integer> usersTable = new HashMap<String, Integer>();

        //Adds users to the select box
        for(int i = 0; i<table.Columns.get(0).values.size(); i++)
        {
            usersBox.addItem(table.Columns.get(1).values.get(i));
            usersTable.put(table.Columns.get(1).values.get(i), Integer.valueOf(table.Columns.get(0).values.get(i)));
        }

        //Usersbox combo box action listener
        usersBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedUsername = (String) usersBox.getSelectedItem(); //get selected username
                System.out.println(selectedUsername);

                try {
                    recipient = new User(usersTable.get(selectedUsername)); //get recipient user obj
                    messagesArea.setText("");                               //clear messages area
                    this.setListenerThread(recipient);
                    System.out.println("Thread for listening set...");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            //creates and starts a new thread for listening messages
            private void setListenerThread(User user) {
                System.out.println("initiated");
                List<String> messages = new ArrayList<>(); //to keep message history
                Thread t = new Thread(() -> { //creating new thread
                    try{
                        System.out.println("Thread initiated");
                        messagesArea.setRows(100);
                        while(true)
                        {
                            System.out.println("Username: " + user.Columns.get(1).values.get(0) + " (id: " + user.Columns.get(0).values.get(0) + ")");
                            Message message = new Message(user);    //check for new messages
                            if(message.Columns.get(0).values.size() > 0) //if there are new messages
                            {
                                List<String> contents = message.Columns.get(1).values; //get contents of those new messages
                                for(String content : contents) //iterate over new messages
                                {
                                    System.out.println(content);
                                    messages.add(content); //add them to message history

                                    //string for printing message history
                                    String msgHistory = "";
                                    for(String msg : messages)
                                    {
                                        msgHistory += user.Columns.get(1).values.get(0) + " : " + msg + "\n";
                                    }


                                    messagesArea.setText(msgHistory); //prints message history
                                }

                                //set new message's status from unread -> read
                                int index = 0;
                                for(String readStatus : message.Columns.get(5).values)
                                {
                                    if(readStatus.equals("false"))
                                    {
                                        message.Columns.get(5).values.set(index, "true");
                                    }
                                    index++;
                                }

                                message.InsertAllRows(); //update DB
                            }

                            //Close DB connection
                            message.disconnect();

                            //Delay for 2s
                            Thread.sleep(2000);
                        }
                    }catch (InterruptedException e)
                    {
                        try {
                            throw e;
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }finally {
                        try {
                            JDBCConnection.connection.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                t.start();
            }
        });

        //Send button action listener
        sendMessageBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    String messageText = textField1.getText(); //get message contents
                    textField1.setText(""); //clear message field
                    try{
                        //create new message object
                        Message message = new Message(
                                messageText,
                                current,
                                recipient.Columns.get(1).values.get(0)
                        );

                        message.send(); //send a message
                        message.disconnect(); //close DB connection
                    } catch (Exception ex) {
                        System.out.println("Sending message failed");
                        System.out.println(ex);
                    }
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                //dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                dispose(); //Destroy the JFrame object
            }
        });

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - dczcx
    private JScrollPane scrollPane1;
    private JTextArea messagesArea;
    private JTextField textField1;
    private JButton sendMessageBtn;
    private JLabel label1;
    private JLabel usernameLabel;
    private JButton logoutBtn;
    private JLabel label2;
    private JComboBox usersBox;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    private User recipient;
}
