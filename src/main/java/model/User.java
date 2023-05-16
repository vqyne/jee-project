package model;

import org.mindrot.jbcrypt.BCrypt;

public class User {
	private int id;
	private String login;
	private String hashedPassword;
	private String salt;
	private CategorieUser category;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}
	
	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void setHashedPassword(String password) {
		setSalt(BCrypt.gensalt());
		this.hashedPassword = BCrypt.hashpw(password, salt);
	}

	public CategorieUser getCategory() {
		return category;
	}

	public void setCategory(CategorieUser category) {
		this.category = category;
	}

	public boolean checkPassword(String password) {
		String hashed = BCrypt.hashpw(password, salt);
		return BCrypt.checkpw(hashed, this.hashedPassword);
	}
}
