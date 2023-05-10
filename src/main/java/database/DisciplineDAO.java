package database;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DisciplineDAO {
	
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
	        System.err.println("Error inserting athlete: " + e.getMessage());
	    } finally {
	        // Clean up resources
	        DBManager.getInstance().cleanup(connection, statement, null);
	    }

	    return ret;
	}
	
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
	            System.out.println("Discipline bien supprimé de la base de données.");
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

	public List<Discipline> findAll(){
		List<Discipline> ret = new ArrayList<Discipline>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			Statement statement = connexion.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM discipline");
			while(rs.next()) {
				String name = rs.getString("name");
				Integer flag = rs.getInt("flag");
				ret.add(new Discipline(name,flag == 1 ? true : false));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
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
	
	
}
