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

/**
 * DAO (Data Access Object) de Athlete
 * Cette classe regroupe les différentes requêtes SQL pour récupérer, ajouter, modifier, supprimer les données
 */
public class AthleteDAO {
	
	//DAO de Discipline nécessaire pour certaines opérations
	private DisciplineDAO disciplineDAO = new DisciplineDAO();

	/**
	 * Fonction permettant d'ajouter un athlète dans la base de données
	 * @param athlete athlète à ajouter
	 * @return true si l'athlète a été ajouté dans la base de données
	 */
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
	
	/**
	 * Fonction permettant de récupérer tous les athlètes de la base de données
	 * @return liste des athlètes présent dans la base de données
	 */
	public List<Athlete> findAll() {
	    List<Athlete> ret = new ArrayList<Athlete>();
	    Connection connexion = DBManager.getInstance().getConnection();
	    try {
	        Statement statement = connexion.createStatement();
	        String sql = "SELECT a.*, d.* FROM athlete a JOIN discipline d ON a.discipline = d.name";
	        ResultSet rs = statement.executeQuery(sql);
	        while(rs.next()) {
	            Integer id = rs.getInt("id");
	            String lastname = rs.getString("lastname");
	            String firstname = rs.getString("firstname");
	            String country = rs.getString("country");
	            Date birthdate = rs.getDate("birthdate");
	            Genre genre = Genre.valueOf(rs.getString("genre"));
	            Discipline discipline = new Discipline(rs.getString("d.name"), rs.getBoolean("d.flag")); 

	            ret.add(new Athlete(id,lastname,firstname,country,birthdate,genre,discipline));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.getInstance().cleanup(connexion, null, null);
	    }
	    return ret;
	}
	
	/**
	 * Fonction permettant de récupérer tous les athlètes de la base de données pour la pagination 
	 * @param limit nombre d'athlètes maximum à retourner
	 * @param page page sur laquelle se trouve l'utilisateur
	 * @return liste des athlètes présent dans la base de données en fonction de la page où se trouve l'utilisateur
	 */
	public List<Athlete> findAllPagination(int limit, int page) {
	    List<Athlete> ret = new ArrayList<Athlete>();
	    Connection connexion = DBManager.getInstance().getConnection();
	    PreparedStatement statement = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT a.*, d.* FROM athlete a JOIN discipline d ON a.discipline = d.name LIMIT ? OFFSET ?";
	        statement = connexion.prepareStatement(sql);
	        statement.setInt(1, limit);
	        //permet de faire un décalage en fonction de la page sur laquelle se trouve l'utilisateur
	        statement.setInt(2, (page - 1) * limit);
	        rs = statement.executeQuery();

	        while (rs.next()) {
	            Integer id = rs.getInt("id");
	            String lastname = rs.getString("lastname");
	            String firstname = rs.getString("firstname");
	            String country = rs.getString("country");
	            Date birthdate = rs.getDate("birthdate");
	            Genre genre = Genre.valueOf(rs.getString("genre"));
	            Discipline discipline = new Discipline(rs.getString("d.name"), rs.getBoolean("d.flag")); 

	            ret.add(new Athlete(id, lastname, firstname, country, birthdate, genre, discipline));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.getInstance().cleanup(connexion, statement, rs);
	    }

	    return ret;
	}

	/**
	 * Fonction permettant de trouver un athlète grâce à son identifiant
	 * @param id identifiant de l'athlète à retourner
	 * @return l'athlète dont l'identifiant est id
	 */
	public Athlete findById(int id) {
	    Athlete ret = null;
	    Connection connexion = DBManager.getInstance().getConnection();
	    try {
	        String sql = "SELECT a.*, d.* FROM athlete a JOIN discipline d ON a.discipline = d.name WHERE a.id = ?";
	        PreparedStatement ps = connexion.prepareStatement(sql);
	        ps.setInt(1,id);
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            String lastname = rs.getString("lastname");
	            String firstname = rs.getString("firstname");
	            String country = rs.getString("country");
	            Date birthdate = rs.getDate("birthdate");
	            Genre genre = Genre.valueOf(rs.getString("genre"));
	            Discipline discipline = new Discipline(rs.getString("d.name"), rs.getBoolean("d.flag"));

	            ret = new Athlete(id,lastname,firstname,country,birthdate,genre,discipline);
	            break;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.getInstance().cleanup(connexion, null, null);
	    }
	    return ret;
	}

	/**
	 * Fonction permettant la recherche grâce à la barre de recherche
	 * @param name texte tapé dans la barre de recherche
	 * @return athlètes correspondant à la recherche effectuée
	 */
	public List<Athlete> findByName(String name) {
		List<Athlete> ret = new ArrayList<Athlete>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			String[] names = name.split(" ");
			String lastname = "";
			String firstname = "";

			//on ne sait pas si la recherche correspond au prénom ou au nom on va rechercher sur les deux
			if(names.length == 2) {
			    lastname = names[1];
			    firstname = names[0];
			} else if(names.length == 1) {
			    lastname = names[0];
			    firstname = names[0];
			}

			
			PreparedStatement ps = connexion.prepareStatement("SELECT * FROM athlete WHERE upper(lastname) LIKE ? OR upper(firstname) LIKE ?");
			ps.setString(1, "%" + lastname.toUpperCase() + "%");
			ps.setString(2, "%" + firstname.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				lastname = rs.getString("lastname");
				firstname = rs.getString("firstname");
				String country = rs.getString("country");
				Date birthdate = rs.getDate("birthdate");
				Genre genre = Genre.valueOf(rs.getString("genre"));
				Discipline discipline = disciplineDAO.findByString(rs.getString("discipline"));
				
				ret.add(new Athlete(id,lastname,firstname,country,birthdate,genre,discipline));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Fonction permettant de chercher les athlètes par discipline
	 * @param disciplineName discipline dont on cherche les athlètes
	 * @return tous les athlètes effectuant la discipline passé en argument
	 */
	public List<Athlete> findByDiscipline(String disciplineName) {
		List<Athlete> ret = new ArrayList<Athlete>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			PreparedStatement ps = connexion.prepareStatement("SELECT * FROM athlete WHERE upper(discipline) = ?");
			ps.setString(1,disciplineName.toUpperCase());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String lastname = rs.getString("lastname");
				String firstname = rs.getString("firstname");
				String country = rs.getString("country");
				Date birthdate = rs.getDate("birthdate");
				Genre genre = Genre.valueOf(rs.getString("genre"));
				Discipline discipline = disciplineDAO.findByString(disciplineName);
				
				ret.add(new Athlete(id,lastname,firstname,country,birthdate,genre,discipline));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Méthode permettant de supprimer un athlète dans la base de données
	 * @param id id de l'athlète (CLEF PRIMAIRE)
	 * @return true si la suppression a fonctionné sinon false
	 */
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
}
