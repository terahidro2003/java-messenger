package GUI;

import database.CRUD.CRUDOperation;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JPanel{
    //declare Swing components
    static JFrame Loginframe = new JFrame("Login | Messenger App v0.2");
    static JFrame signup;
    private JLabel loginStatusLabel;
    private JButton loginBtn;
    private JButton signupBtn;
    private JTextField usernameField;
    private JTextField passwordField;
    private JLabel usernameFieldLabel;
    private JLabel passwordFieldLabel;

    //Constructor
    public LoginFrame(){
        //Construct GUI components
        loginBtn = new JButton("Login");
        signupBtn = new JButton("Signup");
        usernameField = new JTextField("");
        passwordField = new JTextField("");
        usernameFieldLabel = new JLabel("Username: ");
        passwordFieldLabel = new JLabel("Password: ");
        loginStatusLabel = new JLabel("Please login");

        //adjust size and set the layout
        setPreferredSize (new Dimension(255,200));
        setLayout(null);

        //add components to the Swing frame
        add(loginBtn);
        add(signupBtn);
        add(usernameField);
        add(passwordField);
        add(usernameFieldLabel);
        add(passwordFieldLabel);
        add(loginStatusLabel);

        //add components
        loginBtn.setBounds(25,130,205,20);
        signupBtn.setBounds(25,155,205,20);
        usernameField.setBounds(25,40,205,25);
        passwordField.setBounds(25,95,205,25);
        usernameFieldLabel.setBounds(25,20,125,25);
        passwordFieldLabel.setBounds(25,70,125,25);
        loginStatusLabel.setBounds(25,2,125,15);

        //button action listener
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //receive user's input
                String username = usernameField.getText();
                String password = passwordField.getText();

                //simple validation
                if(username.length() > 2 && password.length() > 2)
                {
                    try {
                        User user = new User(username, password); //search for user
                        if(!user.loggedIn()) //if user doesn't exist
                        {
                            loginStatusLabel.setText("Incorrect user details provided!");
                        }else{              //if user exist
                            //close login frame
                            closeLoginFrame();

                            //open MainWindow frame
                            MainWindow mw = new MainWindow(user);
                            mw.setVisible(true);
                            mw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        }

                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        //Signup button action listener
        signupBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //open signup frame
                signup = new JFrame("Signup");
                signup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                signup.getContentPane().add(new SignupFrame());
                signup.setResizable(false);
                signup.pack();
                signup.setVisible(true);
            }
        });
    }

    public static void main(String args[]) {
        //init login frame
        Loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Loginframe.getContentPane().add(new LoginFrame());
        Loginframe.setResizable(false);
        Loginframe.pack();
        Loginframe.setVisible(true);
    }

    //Closes login frame
    private static void closeLoginFrame()
    {
        Loginframe.setVisible(false);
        Loginframe.dispose();
    }

    //Closes signup frame
    protected static void closeSignupFrame(){
        signup.setVisible(false);
        signup.dispose(); //Destroy the JFrame object
    }
}


