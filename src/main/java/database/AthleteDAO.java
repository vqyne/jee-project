package database;
import model.Athlete;
import java.util.ArrayList;
import java.util.List;
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
}
