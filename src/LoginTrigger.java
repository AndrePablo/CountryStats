import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * Authors: Pablo, Ashwin
 */


//Class used to show login window to user and call authentication methods from LoginServer
//Upon successful login, navigate user to MainUI 

public class LoginTrigger  {

	private static JLabel userLabel;
	private static JTextField userText;
	private static JLabel passwordLabel;
	private static JPasswordField passwordText;
	private static JButton button;
	private static JLabel success;

	public static void main(String[] args) {
		showLoginViewer();	
	}
	
	public static void showLoginViewer() {

		//Access LoginServer for authentication 
		
		LoginServer loginServer = new LoginServer();
		
		//Set up for the login window 
		
		JPanel panel = new JPanel();
		JFrame loginFrame = new JFrame();
		loginFrame.setSize(100, 100);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setVisible(true);
		loginFrame.add(panel);

		panel.setLayout(null);

		userLabel = new JLabel("User");
		userLabel.setBounds(10, 20, 80, 25);
		panel.add(userLabel);

		userText = new JTextField(20);
		userText.setBounds(100, 20, 165, 25);
		panel.add(userText);

		passwordLabel = new JLabel("password");
		passwordLabel.setBounds(10, 50, 80, 25);
		panel.add(passwordLabel);

		passwordText = new JPasswordField();
		passwordText.setBounds(100, 50, 165, 25);
		panel.add(passwordText);

		button = new JButton("Login");
		button.setBounds(10, 80, 80, 25);
		panel.add(button);

		success = new JLabel("");
		success.setBounds(10, 110, 300, 25);
		panel.add(success);

		loginFrame.setVisible(true);

		//Upon clicking the login button, authentication service from LoginServer gets called
		//If login is successful, then the user gets access to MainUI 
		//Else an error window is shown 
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = userText.getText();
				String pass = new String(passwordText.getPassword());

				if(loginServer.validateCredentials(user, pass)) {
					JFrame frame = MainUI.getInstance();
			        frame.setSize(900, 600);
			        frame.pack();
			        frame.setVisible(true);
			        loginFrame.setVisible(false);
				}
				
				else {
					loginError();
				}		  
			}
		});	
	}

	//Function to display error message to user 
	
	public static void loginError() {
		  JOptionPane errorPane = new JOptionPane();
		  JOptionPane.showMessageDialog(errorPane, " Login Invalid ");  
	}	
}

