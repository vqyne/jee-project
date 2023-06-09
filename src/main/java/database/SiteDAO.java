package database;

import model.CategorieSite;
import model.Site;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * DAO (Data Access Object) de Site
 * Cette classe regroupe les différentes requêtes SQL pour récupérer, ajouter, modifier, supprimer les données
 */
public class SiteDAO {
	
	/**
	 * Fonction permettant d'ajouter un site à la base de données
	 * @param site site que l'on veut ajouter à la base de données
	 * @return true si l'ajout est un succès false sinon
	 */
	public boolean addSite(Site site) {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    boolean ret = false;

	    try {
	        connection = DBManager.getInstance().getConnection();
	        String sql = "INSERT INTO site (name, city, category) VALUES (?, ?, ?)";
	        statement = connection.prepareStatement(sql);

	        statement.setString(1, site.getName());
	        statement.setString(2, site.getCity());
	        statement.setString(3, site.getCategory().toString());

	        int rowsInserted = statement.executeUpdate();

	        if (rowsInserted > 0) {
	            System.out.println("Site bien ajouté à la base de données.");
	            ret = true;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error inserting site: " + e.getMessage());
	    } finally {
	        // Clean up resources
	        DBManager.getInstance().cleanup(connection, statement, null);
	    }

	    return ret;
	}
	
	/**
	 * Méthode permettant de modifier un site en base de données
	 * @param site site à modifier (seul l'id reste constant)
	 * @return true si le changement est un succès false sinon
	 */
	public boolean editSite(Site site) {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    boolean ret = false;

	    try {
	        connection = DBManager.getInstance().getConnection();
	        
	        String sql = "UPDATE site SET name = ?, city = ?, category = ? WHERE id = ?";
	        statement = connection.prepareStatement(sql);

	        statement.setString(1, site.getName());
	        statement.setString(2, site.getCity());
	        statement.setString(3, site.getCategory().toString());
	        statement.setInt(4, site.getId());

	        int rowsUpdated = statement.executeUpdate();

	        if (rowsUpdated > 0) {
	            System.out.println("Site mis à jour dans la base de données.");

	            
	            ret = true;
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("Error updating site: " + e.getMessage());
	    } finally {
	        DBManager.getInstance().cleanup(connection, statement, null);
	    }

	    return ret;
	}

	/**
	 * Méthode permettant de supprimer un site de la base de données
	 * @param id id du site que l'on souhaite modifier
	 * @return true si la suppression est un succès sinon false
	 */
	public boolean removeSite(int id) {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    boolean ret = false;

	    try {
	        connection = DBManager.getInstance().getConnection();
	        String sql = "DELETE FROM site WHERE id=?";
	        statement = connection.prepareStatement(sql);

	        statement.setInt(1,id);

	        int rowsDeleted = statement.executeUpdate();

	        if (rowsDeleted > 0) {
	            System.out.println("Site bien supprimé de la base de données.");
	            ret = true;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error deleting site: " + e.getMessage());
	    } finally {
	        // Clean up resources
	        DBManager.getInstance().cleanup(connection, statement, null);
	    }

	    return ret;
	}

	/**
	 * Méthode permettant de récupérer tous les sites de la base de données
	 * @return la liste des sites présent en base de données
	 */
	public List<Site> findAll(){
		List<Site> ret = new ArrayList<Site>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			Statement statement = connexion.createStatement();
			ResultSet rs = statement.executeQuery("SELECT site.id, site.name, site.city, site.category, COUNT(session.site) AS session_count " +
	                "FROM site " +
	                "LEFT JOIN session ON site.id = session.site " +
	                "GROUP BY site.id");
			while(rs.next()) {
				int id = rs.getInt("site.id");
				String name = rs.getString("site.name");
				String city = rs.getString("site.city");
				String categoryString = rs.getString("site.category");
				CategorieSite category = CategorieSite.valueOf(categoryString.toLowerCase());
				boolean hasSessions = false;
				if(rs.getInt("session_count") > 0) {
					hasSessions = true;
				}
				Site newSite = new Site(id,name, city, category);
				newSite.setHasSessions(hasSessions);
				ret.add(newSite);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Méthode permettant de retourner les 5 sites les plus utilisés par des sessions
	 * @return le TOP 5 des sites les plus utilisés
	 */
	public List<Site> findTopFiveSitesBySessions() {
	    List<Site> topSites = new ArrayList<>();
	    Connection connection = DBManager.getInstance().getConnection();
	    try {
	        Statement statement = connection.createStatement();
	        ResultSet rs = statement.executeQuery("SELECT site.*, COUNT(session.site) AS session_count " +
	                                               "FROM site " +
	                                               "JOIN session ON site.id = session.site " +
	                                               "GROUP BY site.id " +
	                                               "ORDER BY session_count DESC " +
	                                               "LIMIT 5");
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String name = rs.getString("name");
	            String city = rs.getString("city");
	            String categoryString = rs.getString("category");
	            CategorieSite category = CategorieSite.valueOf((categoryString.toLowerCase()));
	            Site site = new Site(id, name, city, category);
	            site.setNumberUsed(rs.getInt("session_count"));
	            topSites.add(site);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return topSites;
	}
	
	public List<Site> findByString(String searchText) {
		List<Site> ret = new ArrayList<Site>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			PreparedStatement ps = connexion.prepareStatement("SELECT * FROM site WHERE upper(name) LIKE ?");
			ps.setString(1, "%" + searchText.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String city = rs.getString("city");
				String categoryString = rs.getString("category");
				CategorieSite category = CategorieSite.valueOf(categoryString);
				ret.add(new Site(id, name, city, category));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public Site findById(int id) {
		Site ret = null;
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			PreparedStatement ps = connexion.prepareStatement("SELECT * FROM site WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String name = rs.getString("name");
				String city = rs.getString("city");
				String categoryString = (rs.getString("category")).toLowerCase();
				CategorieSite category = CategorieSite.valueOf(categoryString);
				ret = new Site(id, name, city, category);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
