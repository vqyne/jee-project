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

public class UserDAO {

    private static final String TABLE_NAME = "user";

    // Method to create a new user
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

    // Method to retrieve a user by their ID
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

    // Method to retrieve all users
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

    // Method to retrieve a user by their username
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

    // Method to update an existing user
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

    // Method to delete a user
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

    // Utility method to extract a User object from the ResultSet
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
