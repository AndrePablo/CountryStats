import org.jasypt.util.text.BasicTextEncryptor;

/*
 * Authors: Pablo, Ashwin
 */


//Database to store all encrypted user information
//Implements singleton design pattern to ensure only one database can exist 
//Use Jaspyt library for abstracting away encryption 

public class LoginDatabase {
	private String userNames[] = { "Pablo", "Ashwin", "Byron", "Ashvinder" };
	private String passWords[] = new String[4];
	private static LoginDatabase instance = null;
	
	//Use password-based encryption, therefore require unique secret key access to ensure encrypt and decrypt text for a session
	//Require access to key when creating new sessions (instances of BasicTextEncryptor) to decrypt data
	
	private String secretPass = "secret-key";

	//Encrypt information upon initial creation 
	
	private LoginDatabase() {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(secretPass);
		passWords[0] = encryptor.encrypt("250908940");
		passWords[1] = encryptor.encrypt("250897589");
		passWords[2] = encryptor.encrypt("250789456");
		passWords[3] = encryptor.encrypt("254789123");
	}
	
	public static LoginDatabase getInstance() {
		if(instance == null) {
			instance = new LoginDatabase();
		}
		return instance;
	}
	
	//Return user information when requested 
	
	public String[] getUsers() {
		return userNames;
	}
	
	public String[] getPasswords() {
		return passWords;
	}
	
	public String getKey() {
		return secretPass;
	}
}
