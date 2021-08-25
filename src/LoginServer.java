import org.jasypt.util.text.BasicTextEncryptor;

/*
 * Authors: Pablo, Ashwin
 */


//LoginServer accesses the LoginDatabase when a user tries signing in
//It authenticates user-provided data against data in the database, and notifies LoginTrigger of result

public class LoginServer {
	private LoginDatabase db;
	
	public LoginServer() {
		db = LoginDatabase.getInstance();	
	}
	
	public boolean validateCredentials(String username, String pass) {
		String[] users = db.getUsers();
		String[] passwords = db.getPasswords();
		
		//Enter secret key to for session access to data encrypted in LoginDatabase 
		
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(db.getKey());
		
		for(int i = 0; i < users.length; i++) {
			if (users[i].equals(username) && encryptor.decrypt(passwords[i]).equals(pass)) {
				return true;
			}
		}
		return false;
	}

}
  

