package database;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import model.*;

/**
 * DAO (Data Access Object) de Session
 * Cette classe regroupe les différentes requêtes SQL pour récupérer, ajouter, modifier, supprimer les données
 */
public class SessionDAO {
	
	/**
	 * Méthode permettant de retourner toutes les sessions de la base de données
	 * @return toutes les sessions dans la base de données
	 */
	public List<Session> findAll() {
	    List<Session> ret = new ArrayList<Session>();
	    Connection connection = DBManager.getInstance().getConnection();
	    try {
	        Statement statement = connection.createStatement();
	        String query = "SELECT s.*, d.*, si.* " +
	                       "FROM session s " +
	                       "JOIN discipline d ON s.discipline = d.name " +
	                       "JOIN site si ON s.site = si.id";
	        ResultSet rs = statement.executeQuery(query);
	        while (rs.next()) {
	            String code = rs.getString("s.code");
	            Date date = rs.getDate("s.date");
	            LocalTime fromHour = rs.getTime("s.fromHour").toLocalTime();
	            LocalTime toHour = rs.getTime("s.toHour").toLocalTime();
	            Discipline discipline = new Discipline(rs.getString("d.name"), rs.getBoolean("d.flag"));
	            Site site = new Site(rs.getInt("si.id"), rs.getString("si.name"), rs.getString("si.city"), CategorieSite.valueOf((rs.getString("si.category").toLowerCase())));
	            String description = rs.getString("s.description");
	            TypeSession type = TypeSession.valueOf(rs.getString("s.type"));
	            CategorieSession category = CategorieSession.valueOf((rs.getString("s.category").toLowerCase()));
	            ret.add(new Session(code, date, fromHour, toHour, discipline, site, description, type, category));
	        }
	    } catch (SQLException e) {
	        System.err.println("Error finding sessions: " + e.getMessage());
	    } finally {
	        // Clean up resources
	        DBManager.getInstance().cleanup(connection, null, null);
	    }

	    return ret;
	}

	/**
	 * Méthode permettant de retourner toutes les sessions ayant un code semblable à code 
	 * Méthode utilisée pour la barre de recherche
	 * @param code recherche effectuée par l'utilisateur
	 * @return liste des sessions compatibles avec l'argument
	 */
	public List<Session> findByCode(String code) {
		List<Session> ret = new ArrayList<Session>();
	    Connection connection = DBManager.getInstance().getConnection();
	    try {
			PreparedStatement ps = connection.prepareStatement("SELECT s.*, d.*, si.* " +
                    "FROM session s " +
                    "JOIN discipline d ON s.discipline = d.name " +
                    "JOIN site si ON s.site = si.id " +
                    "WHERE s.code LIKE ? ");
			ps.setString(1, "%" + 
                    code + "%");
	        ResultSet rs =  ps.executeQuery();
	        while (rs.next()) {
	        	code = rs.getString("code");
	            Date date = rs.getDate("s.date");
	            LocalTime fromHour = rs.getTime("s.fromHour").toLocalTime();
	            LocalTime toHour = rs.getTime("s.toHour").toLocalTime();
	            Discipline discipline = new Discipline(rs.getString("d.name"), rs.getBoolean("d.flag"));
	            Site site = new Site(rs.getInt("si.id"), rs.getString("si.name"), rs.getString("si.city"), CategorieSite.valueOf((rs.getString("si.category").toLowerCase())));
	            String description = rs.getString("s.description");
	            TypeSession type = TypeSession.valueOf(rs.getString("s.type"));
	            CategorieSession category = CategorieSession.valueOf((rs.getString("s.category").toLowerCase()));
	            ret.add(new Session(code, date, fromHour, toHour, discipline, site, description, type, category));
	        }
	    } catch (SQLException e) {
	        System.err.println("Error finding sessions: " + e.getMessage());
	    } finally {
	        // Clean up resources
	        DBManager.getInstance().cleanup(connection, null, null);
	    }

	    return ret;
	}
    
	/**
	 * Méthode permettant de retourner toutes les sessions d'une discipline
	 * @param discipline discipline dont on veut retourner les sessions
	 * @return toutes les sessions de discipline
	 */
    public List<Session> findByDiscipline(Discipline discipline) {
        List<Session> ret = new ArrayList<Session>();
        Connection connection = DBManager.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM session WHERE upper(discipline) = ?");
            ps.setString(1, discipline.getName().toUpperCase());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String sessionCode = rs.getString("code");
                Date sessionDate = rs.getDate("date");
                LocalTime sessionFromHour = rs.getTime("fromHour").toLocalTime();
                LocalTime sessionToHour = rs.getTime("toHour").toLocalTime();
                String sessionDescription = rs.getString("description");
                Site site = new SiteDAO().findById(rs.getInt("site"));
                String sessionType = (rs.getString("type")).toLowerCase();
                String sessionCategory = (rs.getString("category")).toLowerCase();
                ret.add(new Session(sessionCode, sessionDate, sessionFromHour, sessionToHour, discipline, site, sessionDescription, TypeSession.valueOf(sessionType), CategorieSession.valueOf(sessionCategory)));
            }
        } catch (SQLException e) {
            System.err.println("Error finding session by code: " + e.getMessage());
        } finally {
            // Clean up resources
            DBManager.getInstance().cleanup(connection, null, null);
        }
        return ret;
    }
    
    /**
     * Fonction permettant de retourner toutes les sessions se déroulant à une date précise
     * @param date date des sessions que l'on veut retourner
     * @return liste des sessions se déroulant à la date date
     */
    public List<Session> findByDate(Date date) {
        List<Session> ret = new ArrayList<Session>();
        Connection connection = DBManager.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM session WHERE date = ?");
            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String sessionCode = rs.getString("code");
                LocalTime sessionFromHour = (rs.getTime("fromHour")).toLocalTime();
                LocalTime sessionToHour = (rs.getTime("toHour")).toLocalTime();
                String sessionDescription = rs.getString("description");
                Site site = new SiteDAO().findById(rs.getInt("site"));
                String disciplineName = rs.getString("discipline");
                Discipline discipline = new DisciplineDAO().findByString(disciplineName);
                String sessionType = rs.getString("type");
                String sessionCategory = rs.getString("category");
                ret.add(new Session(sessionCode, date, sessionFromHour, sessionToHour, discipline, site, sessionDescription, TypeSession.valueOf(sessionType), CategorieSession.valueOf(sessionCategory)));
            }
        } catch (SQLException e) {
            System.err.println("Error finding session by code: " + e.getMessage());
        } finally {
            // Clean up resources
            DBManager.getInstance().cleanup(connection, null, null);
        }
        return ret;
    }

	/**
	 * Ajoute une session à la base de données
	 * @param session session à ajouter à la base de données
	 * @return 0 si l'ajout a échoué pour une raison X, 1 si l'ajout est un succès, 2 si l'ajout a échoué à cause d'un chevauchement des épreuves
	 */
    public int addSession(Session session) {
        Connection connection = null;
        PreparedStatement statement = null;
        int ret = 0;

        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "INSERT INTO session (code, date, fromHour, toHour, discipline, site, description, type, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);

            statement.setString(1, session.getCode());
            statement.setDate(2, (Date) new java.sql.Date(session.getDate().getTime()));
            statement.setTime(3, java.sql.Time.valueOf(session.getFromHour()));
            statement.setTime(4, java.sql.Time.valueOf(session.getToHour()));
            statement.setString(5, session.getDiscipline().getName());
            statement.setInt(6, session.getSite().getId());
            statement.setString(7, session.getDescription());
            statement.setString(8, session.getType().toString());
            statement.setString(9, session.getCategory().toString());
            
            if(slotsAvailable(session)) {
            	   int rowsInserted = statement.executeUpdate();

                   if (rowsInserted > 0) {
                       System.out.println("Session bien ajoutée à la base de données.");
                       ret = 1;
                   }
            } else {
            	System.out.println("La session n'a pas pu être ajoutée car une session de " + session.getDiscipline().getName() + " est déjà programmée sur ce créneau");
                ret = 2;
            }

         
        } catch (SQLException e) {
            System.err.println("Error inserting session: " + e.getMessage());
        } finally {
            // Clean up resources
            DBManager.getInstance().cleanup(connection, statement, null);
        }
        return ret;

    }
    
    /**
     * Méthode permettant de modifier une session
     * @param session session à modifier (le code reste constant, le reste est peut-être modifié auparavant)
	 * @return 0 si l'ajout a échoué pour une raison X, 1 si l'ajout est un succès, 2 si l'ajout a échoué à cause d'un chevauchement des épreuves
     */
	public int editSession(Session session) {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    int ret = 0;

	    try {
	        connection = DBManager.getInstance().getConnection();
	        
	        String sql = "UPDATE session SET date = ?, fromHour = ?, toHour = ?, discipline = ?, site = ?, description = ?, type = ?, category = ? WHERE code = ?";

	        statement = connection.prepareStatement(sql);

            statement.setDate(1, (Date) new java.sql.Date(session.getDate().getTime()));
            statement.setTime(2, java.sql.Time.valueOf(session.getFromHour()));
            statement.setTime(3, java.sql.Time.valueOf(session.getToHour()));
            statement.setString(4, session.getDiscipline().getName());
            statement.setInt(5, session.getSite().getId());
            statement.setString(6, session.getDescription());
            statement.setString(7, session.getType().toString());
            statement.setString(8, session.getCategory().toString());
            statement.setString(9, session.getCode());


            if(slotsAvailable(session)) {
         	   int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Session bien mise à jour à la base de données.");
                    ret = 1;
                }
            } else {
            	System.out.println("La session n'a pas pu être mise à jour car une session de " + session.getDiscipline().getName() + " est déjà programmée sur ce créneau");
            	ret = 2;
            }
	        
	    } catch (SQLException e) {
	        System.err.println("Error updating site: " + e.getMessage());
	    } finally {
	        DBManager.getInstance().cleanup(connection, statement, null);
	    }

	    return ret;
	}

	/**
	 * Méthode permettant de vérifier qu'une session ne rentre pas en collision au niveau emploi du temps avec d'autres sessions de la même discipline
	 * La méthode est appelée par addSession et editSession
	 * @param session session dont on veut vérifier la compatibilité
	 * @return true si la session peut être ajoutée sans crainte, false sinon
	 */
    private boolean slotsAvailable(Session session) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean available = true;

        try {
            connection = DBManager.getInstance().getConnection();
            //l'espace temps de 1h avant le début de la session jusqu'à 1h après la fin doit être libre
            String sql = "SELECT * FROM session WHERE discipline = ? AND date = ? AND ((fromHour <= ? AND toHour >= ?) OR (fromHour <= ? AND toHour >= ?) OR (toHour < ? AND ADDTIME(toHour, '01:00:00') > ?) OR (fromHour > ? AND SUBTIME(fromHour, '01:00:00') < ?))";
            statement = connection.prepareStatement(sql);
            statement.setString(1, session.getDiscipline().getName());
            statement.setDate(2, new java.sql.Date(session.getDate().getTime()));
            statement.setTime(3, java.sql.Time.valueOf(session.getFromHour()));
            statement.setTime(4, java.sql.Time.valueOf(session.getFromHour()));
            statement.setTime(5, java.sql.Time.valueOf(session.getToHour()));
            statement.setTime(6, java.sql.Time.valueOf(session.getToHour()));
            statement.setTime(7, java.sql.Time.valueOf(session.getToHour()));
            statement.setTime(8, java.sql.Time.valueOf(session.getToHour()));
            statement.setTime(9, java.sql.Time.valueOf(session.getFromHour()));
            statement.setTime(10, java.sql.Time.valueOf(session.getFromHour()));
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                available = false;
            }
        } catch (SQLException e) {
            System.err.println("Error checking session availability: " + e.getMessage());
        } finally {
            // Clean up resources
            DBManager.getInstance().cleanup(connection, statement, resultSet);
        }

        return available;
	}

    /**
     * Méthode permettant de supprimer une session de la base de données
     * @param code code de la session (CLEF PRIMAIRE)
     * @return true si la suppression est un succès false sinon
     */
	public boolean removeSession(String code) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean ret = false;

        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "DELETE FROM session WHERE code=?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, code);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Session bien supprimée de la base de données.");
                ret = true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting session: " + e.getMessage());
        } finally {
            DBManager.getInstance().cleanup(connection, statement, null);
        }

        return ret;
    }


}
