package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.CategorieUser;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * DAO (Data Access Object) de User
 * Cette classe regroupe les différentes requêtes SQL pour récupérer, ajouter, modifier, supprimer les données
 */
public class UserDAO {

    private static final String TABLE_NAME = "user";
    
    /**
     * Méthode retournant tous les utilisateurs dans la base de données
     * @return tous les utilisateurs en base de données
     */
    public List<User> getAllUsers() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<>();

        try {
            connection = DBManager.getInstance().getConnection();
            String query = "SELECT * FROM " + TABLE_NAME;
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.getInstance().cleanup(connection, statement, resultSet);
        }

        return userList;
    }

    /**
     * Méthode retournant un utilisateur ayant pour nom d'utilisateur username
     * @param username nom d'utilisateur de l'utilisateur que l'on veut retourner
     * @return l'utilisateur ayant pour nom d'utilisateur username
     */
    public User getUserByUsername(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DBManager.getInstance().getConnection();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE login = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.getInstance().cleanup(connection, statement, resultSet);
        }

        return user;
    }
    
    /**
     * Méthode retournant un utilisateur ayant pour identifiant id
     * @param userId identifiant de l'utilisateur que l'on veut retourner
     * @return l'utilisateur ayant pour identifiant userId
     */    
    public User getUserById(int userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DBManager.getInstance().getConnection();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.getInstance().cleanup(connection, statement, resultSet);
        }

        return user;
    }

   /**
    * Méthode permettant de créer un nouvel utilisateur en base de données
    * @param user user à ajouter
    * @return true si l'ajout est un succès false sinon
    */
    public boolean createUser(User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;

        try {
            connection = DBManager.getInstance().getConnection();
            String query = "INSERT INTO " + TABLE_NAME + " (login, hashedPassword, salt, category) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getHashedPassword());
            statement.setString(3, user.getSalt());
            statement.setString(4, user.getCategory().name());

            int rowsAffected = statement.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.getInstance().cleanup(connection, statement, null);
        }

        return success;
    }
    
    

    /**
     * Méthode permettant de mettre à jour un utilisateur
     * @param user utilisateur à modifier en base de données
     * @return true si l'opération est un succès false sinon
     */
    public boolean updateUser(User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;

        try {
            connection = DBManager.getInstance().getConnection();
            String query = "UPDATE " + TABLE_NAME
                    + " SET login = ?, hashedPassword = ?, salt = ?, category = ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getHashedPassword());
            statement.setString(3, user.getSalt());
            statement.setString(4, user.getCategory().name());
            statement.setInt(5, user.getId());

            int rowsAffected = statement.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.getInstance().cleanup(connection, statement, null);
        }

        return success;
    }

    /**
     * Méthode permettant de supprimer un utilisateur en base de données
     * @param userId identifiant de l'utilisateur que l'on veut supprimer
     * @return true si la suppression est un succès false sinon
     */
    public boolean deleteUser(int userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;

        try {
            connection = DBManager.getInstance().getConnection();
            String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            int rowsAffected = statement.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.getInstance().cleanup(connection, statement, null);
        }

        return success;
    }

    /**
     * Méthode permettant d'extraire l'objet User du ResultSet
     * @param resultSet resultSet duquel on veut extraire l'utilisateur
     * @return l'utilisateur recherché
     * @throws SQLException exception pouvant être levée lors de l'opération
     */
    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setLogin(resultSet.getString("login"));
        user.setHashedPassword(resultSet.getString("hashedPassword"));
        user.setSalt(resultSet.getString("salt"));
        String categoryString = resultSet.getString("category");
        CategorieUser category = CategorieUser.valueOf(categoryString);
        user.setCategory(category);
        return user;
    }
}
