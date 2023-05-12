package database;
import model.Athlete;
import database.DisciplineDAO;
import model.Discipline;
import model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class AthleteDAO {

	public boolean addAthlete(Athlete athlete) {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    boolean ret = false;

	    try {
	        connection = DBManager.getInstance().getConnection();
	        String sql = "INSERT INTO athlete (lastname, firstname, country, birthdate, genre, discipline) VALUES (?, ?, ?, ?, ?, ?)";
	        statement = connection.prepareStatement(sql);

	        statement.setString(1, athlete.getLastname());
	        statement.setString(2, athlete.getFirstname());
	        statement.setString(3, athlete.getCountry());
	        statement.setDate(4, new java.sql.Date(athlete.getBirthdate().getTime()));
	        statement.setString(5, athlete.getGenreString());
	        statement.setString(6, athlete.getDiscipline().getName());

	        int rowsInserted = statement.executeUpdate();

	        if (rowsInserted > 0) {
	            System.out.println("Athlete bien ajouté à la base de données.");
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
	
	public boolean removeAthlete(int id) {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    boolean ret = false;

	    try {
	        connection = DBManager.getInstance().getConnection();
	        String sql = "DELETE FROM athlete WHERE id=?";
	        statement = connection.prepareStatement(sql);

	        statement.setInt(1, id);

	        int rowsDeleted = statement.executeUpdate();

	        if (rowsDeleted > 0) {
	            System.out.println("Athlete bien supprimé de la base de données.");
	            ret = true;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error deleting athlete: " + e.getMessage());
	    } finally {
	        // Clean up resources
	        DBManager.getInstance().cleanup(connection, statement, null);
	    }

	    return ret;
	}

	
	public List<Athlete> findAll(){
		List<Athlete> ret = new ArrayList<Athlete>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			Statement statement = connexion.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM athlete");
			while(rs.next()) {
				Integer id = rs.getInt("id");
				String lastname = rs.getString("lastname");
				String firstname = rs.getString("firstname");
				String country = rs.getString("country");
				Date birthdate = rs.getDate("birthdate");
				Genre genre = Genre.valueOf(rs.getString("genre"));
				Discipline discipline = new DisciplineDAO().findByString(rs.getString("discipline"));
				
				ret.add(new Athlete(id,lastname,firstname,country,birthdate,genre,discipline));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public Athlete findById(int id) {
		Athlete ret = null;
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			PreparedStatement ps = connexion.prepareStatement("SELECT * FROM athlete WHERE id = ?");
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String lastname = rs.getString("lastname");
				String firstname = rs.getString("firstname");
				String country = rs.getString("country");
				Date birthdate = rs.getDate("birthdate");
				Genre genre = Genre.valueOf(rs.getString("genre"));
				Discipline discipline = new DisciplineDAO().findByString(rs.getString("discipline"));
				
				ret = new Athlete(id,lastname,firstname,country,birthdate,genre,discipline);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public List<Athlete> findByName(String firstname, String lastname) {
		List<Athlete> ret = new ArrayList<Athlete>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			PreparedStatement ps = connexion.prepareStatement("SELECT * FROM athlete WHERE upper(lastname) = ? AND upper(firstname) = ?");
			ps.setString(1, "%" + firstname.toUpperCase() + "%");
			ps.setString(2, "%" + lastname.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String country = rs.getString("country");
				Date birthdate = rs.getDate("birthdate");
				Genre genre = Genre.valueOf(rs.getString("genre"));
				Discipline discipline = new DisciplineDAO().findByString(rs.getString("discipline"));
				
				ret.add(new Athlete(id,lastname,firstname,country,birthdate,genre,discipline));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public List<Athlete> findByDiscipline(Discipline discipline) {
		List<Athlete> ret = new ArrayList<Athlete>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			PreparedStatement ps = connexion.prepareStatement("SELECT * FROM athlete WHERE upper(discipline) = ?");
			ps.setString(1, "%" + discipline.getName().toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String lastname = rs.getString("lastname");
				String firstname = rs.getString("firstname");
				String country = rs.getString("country");
				Date birthdate = rs.getDate("birthdate");
				Genre genre = Genre.valueOf(rs.getString("genre"));
				
				ret.add(new Athlete(id,lastname,firstname,country,birthdate,genre,discipline));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	
}