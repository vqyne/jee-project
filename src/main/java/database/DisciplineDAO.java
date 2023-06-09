package database;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * DAO (Data Access Object) de Discipline
 * Cette classe regroupe les différentes requêtes SQL pour récupérer, ajouter, modifier, supprimer les données
 */
public class DisciplineDAO {
	
	/**
	 * Méthode permettant de retourner toutes les disciplines présentes dans la base de données
	 * @return toutes les disciplines présentes en base de données
	 */
	public List<Discipline> findAll() {
	    List<Discipline> ret = new ArrayList<>();
	    Connection connection = DBManager.getInstance().getConnection();
	    PreparedStatement statement = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT d.name, d.flag, " +
	                "(COUNT(DISTINCT s.code) > 0 OR COUNT(DISTINCT a.id) > 0) AS is_linked " +
	                "FROM discipline d " +
	                "LEFT JOIN session s ON d.name = s.discipline " +
	                "LEFT JOIN athlete a ON d.name = a.discipline " +
	                "GROUP BY d.name, d.flag";
	        statement = connection.prepareStatement(sql);
	        rs = statement.executeQuery();

	        while (rs.next()) {
	            String name = rs.getString("name");
	            int flag = rs.getInt("flag");
	            boolean isLinked = rs.getBoolean("is_linked");

	            Discipline discipline = new Discipline(name, flag == 1);
	            discipline.setLinked(isLinked);
	            ret.add(discipline);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.getInstance().cleanup(connection, statement, rs);
	    }

	    return ret;
	}

	
	/**
	 * Méthode permettant la recherche dans la barre de recherche
	 * @param searchText recherche effectuée par l'utilisateur
	 * @return les disciplines correspondants à la recherche
	 */
	public Discipline findByString(String searchText) {
		Discipline ret = null;
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			PreparedStatement ps = connexion.prepareStatement("SELECT * FROM discipline WHERE upper(name) = ?");
			ps.setString(1,searchText.toUpperCase());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String name = rs.getString("name");
				Integer flag = rs.getInt("flag");
				ret = new Discipline(name,flag == 1 ? true : false);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Méthode permettant de retourner la TOP 5 des disciplines avec les sessions les plus longues
	 * @return le TOP 5
	 */
	public List<Discipline> findTopFiveDisciplinesByDuration() {
	    List<Discipline> topDisciplines = new ArrayList<>();
	    Connection connection = DBManager.getInstance().getConnection();
	    try {
	        Statement statement = connection.createStatement();
	        ResultSet rs = statement.executeQuery("SELECT discipline.name, discipline.flag, SUM(TIMESTAMPDIFF(MINUTE, session.fromHour, session.toHour)) AS duration " +
	                                               "FROM discipline " +
	                                               "JOIN session ON discipline.name = session.discipline " +
	                                               "GROUP BY discipline.name " +
	                                               "ORDER BY duration DESC " +
	                                               "LIMIT 5");
	        while (rs.next()) {
	            String name = rs.getString("name");
	            Integer flag = rs.getInt("flag");
	            Discipline discipline = new Discipline(name, flag == 1);
	            discipline.setAllTime(rs.getInt("duration"));
	            topDisciplines.add(discipline);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return topDisciplines;
	}
	
	/**
	 * Fonction permettant d'ajouter une discipline à la base de données
	 * @param discipline discipline à ajouter
	 * @return retourne true si l'ajout a été effectué false sinon
	 */
	public boolean addDiscipline(Discipline discipline) {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    boolean ret = false;

	    try {
	        connection = DBManager.getInstance().getConnection();
	        String sql = "INSERT INTO discipline (name, flag) VALUES (?, ?)";
	        statement = connection.prepareStatement(sql);

	        statement.setString(1, discipline.getName());
	        statement.setInt(2, discipline.isFlag() ? 1 : 0);

	        int rowsInserted = statement.executeUpdate();

	        if (rowsInserted > 0) {
	            System.out.println("Discipline bien ajouté à la base de données.");
	            ret = true;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error inserting discipline: " + e.getMessage());
	    } finally {
	        // Clean up resources
	        DBManager.getInstance().cleanup(connection, statement, null);
	    }

	    return ret;
	}
	
	/**
	 * Fonction permettant de modifier une discipline
	 * @param name nom de la discipline à modifier (CLEF PRIMAIRE)
	 * @param newName nouveau nom de la discipline
	 * @return true si la modification est un succès false sinon
	 */
	public boolean editDiscipline(String name, String newName) {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    boolean ret = false;

	    try {
	        connection = DBManager.getInstance().getConnection();
	        //desactive les clés étrangères pour pouvoir modifier la discipline
	        String disableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS=0";
	        statement = connection.prepareStatement(disableForeignKeyChecks);
	        statement.executeUpdate();

	        String updateDisciplineSql = "UPDATE discipline SET name = ? WHERE name = ?";
	        statement = connection.prepareStatement(updateDisciplineSql);
	        statement.setString(1, newName);
	        statement.setString(2, name);
	        int rowsUpdated = statement.executeUpdate();

	        if (rowsUpdated > 0) {
	            System.out.println("Discipline bien modifiée dans la base de données.");

	            //modification dans athlete et session le nom de la discipline
	            String updateAthleteSql = "UPDATE athlete SET discipline = ? WHERE discipline = ?";
	            statement = connection.prepareStatement(updateAthleteSql);
	            statement.setString(1, newName);
	            statement.setString(2, name);
	            statement.executeUpdate();

	            String updateSessionSql = "UPDATE session SET discipline = ? WHERE discipline = ?";
	            statement = connection.prepareStatement(updateSessionSql);
	            statement.setString(1, newName);
	            statement.setString(2, name);
	            statement.executeUpdate();

	            ret = true;
	        }

	        String enableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS=1";
	        statement = connection.prepareStatement(enableForeignKeyChecks);
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la mise à jour de la discipline: " + e.getMessage());
	    } finally {
	        DBManager.getInstance().cleanup(connection, statement, null);
	    }

	    return ret;
	}

	/**
	 * Méthode permettant de supprimer une discipline de la base de données
	 * @param name nom de la discipline (CLEF PRIMAIRE) à supprimer
	 * @return true si la suppression de la discipline est un succès
	 */
	public boolean removeDiscipline(String name) {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    boolean ret = false;

	    try {
	        connection = DBManager.getInstance().getConnection();
	        String sql = "DELETE FROM discipline WHERE upper(name)=?";
	        statement = connection.prepareStatement(sql);

	        statement.setString(1, name.toUpperCase());

	        int rowsDeleted = statement.executeUpdate();

	        if (rowsDeleted > 0) {
	            System.out.println("Discipline bien supprimée de la base de données.");
	            ret = true;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error deleting discipline: " + e.getMessage());
	    } finally {
	        // Clean up resources
	        DBManager.getInstance().cleanup(connection, statement, null);
	    }

	    return ret;
	}	
}
