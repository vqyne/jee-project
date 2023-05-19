package database;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DBInitializer implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent sce) {
	    DBManager.getInstance().createTables();
	}

	public void contextDestroyed(ServletContextEvent sce) {
	    // Do nothing
	}
	
}