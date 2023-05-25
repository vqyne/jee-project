import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Classe de configuration définissant le chemin de base pour les méthodes REST (contrôleurs) 
 *
 */
@ApplicationPath("/api")
public class AppConfig extends Application {
	
}
