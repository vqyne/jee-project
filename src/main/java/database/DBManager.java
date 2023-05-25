package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Classe permettant de manager la base de données
 */
public class DBManager {

	private static DBManager instance;

	private ResourceBundle properties;

	private static String resourceBundle = "config";

	/**
	 * Constructeur privé
	 */
	private DBManager() {
		properties = ResourceBundle.getBundle(resourceBundle);

		try {
			Class.forName(properties.getString("DB_DRIVER"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Retourne l'instance en singleton de DBManager
	 * @return l'instance de la classe
	 */
	public static DBManager getInstance() {
		if (instance == null) {
			synchronized (DBManager.class) {
				instance = new DBManager();
			}
		}
		return instance;
	}

	/**
	 * Permet de faire la connexion avec la base de données
	 * @return
	 */
	public Connection getConnection() {

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(properties.getString("JDBC_URL"), properties.getString("DB_LOGIN"),
					properties.getString("DB_PASSWORD"));

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return connection;

	}

	/**
	 * Permet de fermer les objets utilisés dans les DAO
	 * @param connection connexion avec la base de données
	 * @param stat permet de manipuler la requête
	 * @param rs résultat de la requête SQL
	 */
	public void cleanup(Connection connection, Statement stat, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (stat != null) {
			try {
				stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Fonction permettant de créer les tables dans la base de données
	 */
	public void createTables() {
	    Connection conn = null;
	    Statement stmt = null;
	    try {
	        conn = getConnection();
	        stmt = conn.createStatement();

	        // Create Discipline table
	        String disciplineTable = "CREATE TABLE IF NOT EXISTS discipline ("
	                + "name VARCHAR(255) NOT NULL,"
	                + "flag TINYINT(1) NOT NULL,"
	                + "PRIMARY KEY (name)"
	                + ");";
	        stmt.executeUpdate(disciplineTable);
	        
	        // Create Athlete table
	        String athleteTable = "CREATE TABLE IF NOT EXISTS athlete ("
	                + "id INT NOT NULL AUTO_INCREMENT,"
	                + "lastname VARCHAR(255) NOT NULL,"
	                + "firstname VARCHAR(255) NOT NULL,"
	                + "country VARCHAR(255) NOT NULL,"
	                + "birthdate DATE NOT NULL,"
	                + "genre VARCHAR(255) NOT NULL,"
	                + "discipline VARCHAR(255) NOT NULL,"
	                + "PRIMARY KEY (id),"
	                + "FOREIGN KEY (discipline) REFERENCES discipline(name)"
	                + ");";
	        stmt.executeUpdate(athleteTable);
	        
	        // Create Site table
	        String siteTable = "CREATE TABLE IF NOT EXISTS site ("
	                + "id INT NOT NULL AUTO_INCREMENT,"
	                + "name VARCHAR(255) NOT NULL,"
	                + "city VARCHAR(255) NOT NULL,"
	                + "category VARCHAR(255) NOT NULL,"
	                + "PRIMARY KEY (id)"
	                + ");";
	        stmt.executeUpdate(siteTable);

	        // Create Session table
	        String sessionTable = "CREATE TABLE IF NOT EXISTS session ("
	                + "code VARCHAR(255) NOT NULL,"
	                + "date DATE NOT NULL,"
	                + "fromHour TIME NOT NULL,"
	                + "toHour TIME NOT NULL,"
	                + "discipline VARCHAR(255) NOT NULL,"
	                + "site INT NOT NULL,"
	                + "description TEXT NOT NULL,"
	                + "type VARCHAR(255) NOT NULL,"
	                + "category VARCHAR(255) NOT NULL,"
	                + "PRIMARY KEY (code),"
	                + "FOREIGN KEY (discipline) REFERENCES discipline(name),"
	                + "FOREIGN KEY (site) REFERENCES site(id)"
	                + ");";
	        stmt.executeUpdate(sessionTable);


	     // Create User table
	        String userTable = "CREATE TABLE IF NOT EXISTS user ("
	                + "id INT NOT NULL AUTO_INCREMENT,"
	                + "login VARCHAR(255) NOT NULL,"
	                + "hashedPassword VARCHAR(255) NOT NULL,"
	                + "salt VARCHAR(255) NOT NULL,"
	                + "category VARCHAR(255) NOT NULL,"
	                + "PRIMARY KEY (id)"
	                + ");";

	        stmt.executeUpdate(userTable);

	        cleanup(conn, stmt, null);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Méthode principale appelant la fonction créant la connexion
	 * @param args
	 */
	public static void main(String[] args) {
		Connection c = DBManager.getInstance().getConnection();
		if (c != null) {
			try {
				System.out.println("Connection to db : " + c.getCatalog());
				Properties p = c.getClientInfo();
				Enumeration<Object> keys = p.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					System.out.println(key + ":" + p.getProperty(key));
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBManager.getInstance().cleanup(c, null, null);
			}
		}
	}
}

