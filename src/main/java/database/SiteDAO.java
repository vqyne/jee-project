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

public class SiteDAO {
	
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

	public List<Site> findAll(){
		List<Site> ret = new ArrayList<Site>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			Statement statement = connexion.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM site");
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String city = rs.getString("city");
				String categoryString = rs.getString("category");
				CategorieSite category = CategorieSite.valueOf(categoryString.toLowerCase());
				ret.add(new Site(id,name, city, category));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
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
