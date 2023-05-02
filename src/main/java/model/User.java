package model;

public class User {
	private String login;
	private String hashedPassword;
	private CategorieUser category;

	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public CategorieUser getCategory() {
		return category;
	}

	public void setCategory(CategorieUser category) {
		this.category = category;
	}

	public boolean checkPassword(String password) {
		return false;
		//return BCrypt.checkpw(password,this.hashedPassword);
	}
	
	private String hashPassword(String password) {
		return null;
		//return BCrypt.hashpw(password, BCrypt.gensalt())
	}
}
