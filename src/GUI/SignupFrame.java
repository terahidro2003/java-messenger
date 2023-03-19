package GUI;

import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupFrame extends JPanel {
    //register swing components
    static JFrame SignupFrame = new JFrame("New user signup | Messenger App v0.2");
    private JLabel signupStatusLabel;
    private JButton signupBtn;
    private JTextField usernameField;
    private JTextField passwordField;
    private JLabel usernameFieldLabel;
    private JLabel passwordFieldLabel;

    //constructor
    public SignupFrame(){
        //Construct GUI components
        signupBtn = new JButton("Signup");
        usernameField = new JTextField("");
        passwordField = new JTextField("");
        usernameFieldLabel = new JLabel("Username: ");
        passwordFieldLabel = new JLabel("Password: ");
        signupStatusLabel = new JLabel("Please enter new user details");

        //adjust size and set the layout
        setPreferredSize (new Dimension(350,180));
        setLayout(null);

        //add components to the panel
        add(signupBtn);
        add(usernameField);
        add(passwordField);
        add(usernameFieldLabel);
        add(passwordFieldLabel);
        add(signupStatusLabel);

        //add components
        signupBtn.setBounds(25,130,300,20);
        usernameField.setBounds(25,40,300,25);
        passwordField.setBounds(25,95,300,25);
        usernameFieldLabel.setBounds(25,20,300,25);
        passwordFieldLabel.setBounds(25,70,300,25);
        signupStatusLabel.setBounds(25,2,300,15);

        //set event listeners
        signupBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //get user's input
                String username = usernameField.getText();
                String password = passwordField.getText();
                //simple validation
                if(username.length() > 2 && password.length() > 2)
                {
                    try {
                        User user = new User(username, password); //init user obj
                        if(!user.loggedIn()) //if user doesn't exist
                        {
                            signupStatusLabel.setText("User created. You can login now");
                            user.Signup(username, password); //add user to DB
                            //close signup frame
                            setVisible(false);
                            GUI.LoginFrame.closeSignupFrame();
                        }else{
                            signupStatusLabel.setText("This user already exist! Please login");
                        }
                        user = null;

                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
}
