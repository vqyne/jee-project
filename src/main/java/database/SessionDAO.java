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

public class SessionDAO {

    public boolean addSession(Session session) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean ret = false;

        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "INSERT INTO session (code, date, fromHour, toHour, discipline_id, site_id, description, type, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);

            statement.setString(1, session.getCode());
            statement.setDate(2, (Date) session.getDate());
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
                       ret = true;
                   }
            } else {
            	System.out.println("La session n'a pas pu être ajoutée car une session de " + session.getDiscipline().getName() + "est déjà programmé sur ce créneau");
                ret = false;
            }

         
        } catch (SQLException e) {
            System.err.println("Error inserting session: " + e.getMessage());
        } finally {
            // Clean up resources
            DBManager.getInstance().cleanup(connection, statement, null);
        }

        return ret;
    }

    private boolean slotsAvailable(Session session) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean available = true;

        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "SELECT * FROM session WHERE discipline_name = ? AND date = ? AND ((fromHour <= ? AND toHour >= ?) OR (fromHour <= ? AND toHour >= ?) OR (toHour < ? AND ADDTIME(toHour, '01:00:00') > ?) OR (fromHour > ? AND SUBTIME(fromHour, '01:00:00') < ?))";
            statement = connection.prepareStatement(sql);
            statement.setString(1, session.getDiscipline().getName());
            statement.setDate(2, new java.sql.Date(session.getDate().getTime()));
            statement.setTime(3, java.sql.Time.valueOf(session.getFromHour()));
            statement.setTime(4, java.sql.Time.valueOf(session.getFromHour()));
            statement.setTime(5, java.sql.Time.valueOf(session.getToHour()));
            statement.setTime(6, java.sql.Time.valueOf(session.getToHour()));
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
            // Clean up resources
            DBManager.getInstance().cleanup(connection, statement, null);
        }

        return ret;
    }

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
	            Site site = new Site(rs.getInt("si.id"), rs.getString("si.name"), rs.getString("si.city"), CategorieSite.valueOf(rs.getString("si.category").toLowerCase()));
	            String description = rs.getString("s.description");
	            TypeSession type = TypeSession.valueOf(rs.getString("s.type"));
	            CategorieSession category = CategorieSession.valueOf(rs.getString("s.category").toLowerCase());
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

    
	public List<Session> findByCode(String code) {
	    List <Session> ret = null;
	    Connection connection = DBManager.getInstance().getConnection();
	    try {
	        String query = "SELECT s.*, d.name AS discipline_name, d.flag AS discipline_flag, si.id AS site_id, si.name AS site_name, si.city AS site_city, si.category AS site_category " +
	                       "FROM session s " +
	                       "JOIN discipline d ON s.discipline = d.name " +
	                       "JOIN site si ON s.site = si.id " +
	                       "WHERE s.code LIKE ?";
	        PreparedStatement ps = connection.prepareStatement(query);
	        ps.setString(1, code + "%");
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            String sessionCode = rs.getString("s.code");
	            Date sessionDate = rs.getDate("s.date");
	            LocalTime sessionFromHour = rs.getTime("s.fromHour").toLocalTime();
	            LocalTime sessionToHour = rs.getTime("s.toHour").toLocalTime();
	            String sessionDescription = rs.getString("s.description");
	            Discipline discipline = new Discipline(rs.getString("discipline_name"), rs.getBoolean("discipline_flag"));
	            Site site = new Site(rs.getInt("site_id"), rs.getString("site_name"), rs.getString("site_city"), CategorieSite.valueOf(rs.getString("site_category")));
	            String sessionType = rs.getString("s.type");
	            String sessionCategory = rs.getString("s.category");
	            ret.add(new Session(sessionCode, sessionDate, sessionFromHour, sessionToHour, discipline, site, sessionDescription, TypeSession.valueOf(sessionType), CategorieSession.valueOf(sessionCategory));
	            break;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error finding session by code: " + e.getMessage());
	    } finally {
	        // Clean up resources
	        DBManager.getInstance().cleanup(connection, null, null);
	    }
	    return ret;
	}

    
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
                String sessionType = rs.getString("type");
                String sessionCategory = rs.getString("category");
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
    
    public List<Session> findByDate(Date date) {
        List<Session> ret = new ArrayList<Session>();
        Connection connection = DBManager.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM session WHERE date = ?");
            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String sessionCode = rs.getString("code");
                LocalTime sessionFromHour = rs.getTime("fromHour").toLocalTime();
                LocalTime sessionToHour = rs.getTime("toHour").toLocalTime();
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
}
