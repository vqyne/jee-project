package database;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Classe permettant d'initialiser les tables dans la base de données
 */
@WebListener
public class DBInitializer implements ServletContextListener {
	
	/**
	 * Appelle la fonction dans DBManager permettant de créer les tables
	 */
	public void contextInitialized(ServletContextEvent sce) {
	    DBManager.getInstance().createTables();
	}

	public void contextDestroyed(ServletContextEvent sce) {
	    // Do nothing
	}
	
}