package com.studybot.db.dao;

import com.studybot.db.dto.User;
import com.studybot.exceptions.ConfigurationException;
import com.studybot.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PostgresUserDaoImpl extends PostgresAbstractDaoImpl implements UserDao {

    public PostgresUserDaoImpl() throws DatabaseException, ConfigurationException {
    }

    @Override
    public User getUserById(int userId) throws DatabaseException {
        User user = null;
        try(Connection conn = databaseConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ?")){
            stmt.setInt(1, userId);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    user = new User(
                            rs.getInt("UserID"),
                            rs.getString("UserName"),
                            rs.getString("UserType"),
                            rs.getInt("TelegramUserID")
                    );
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Couldn't retrieve user with such id", e);
        }
        return user;
    }

    @Override
    public User getUserByTelegramUserIdAndType(int userId, String userType) throws DatabaseException {
        User user = null;
        try(Connection conn = databaseConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ? AND actor_type = ?")){
            stmt.setInt(1, userId);
            stmt.setString(2, userType);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    user = new User(
                            rs.getInt("UserID"),
                            rs.getString("UserName"),
                            rs.getString("UserType"),
                            rs.getInt("TelegramUserID")
                    );
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Couldn't retrieve user with such user id and type", e);
        }
        return user;
    }

    @Override
    public Set<User> getUsersByType(String type) throws DatabaseException {
        Set<User> users = new HashSet<>();
        try(Connection conn = databaseConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE UserType = ?")){
            stmt.setString(1, type);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("UserID"),
                            rs.getString("UserName"),
                            rs.getString("UserType"),
                            rs.getInt("TelegramUserID")
                    ));
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Couldn't retrieve users by type", e);
        }
        return users;
    }

    private User processSingleResultSet(ResultSet rs) throws SQLException {
        User user = null;
        while (rs.next()) {
             user = new User(
                    rs.getInt("actor_id"),
                    rs.getString("actor_name"),
                    rs.getString("actor_type"),
                    rs.getInt("user_id")
            );
        }
        return user;
    }

    private Set<User> processMultipleResultSet(ResultSet rs) throws SQLException {
        Set<User> users = new HashSet<>();
        while (rs.next()) {
            users.add(new User(
                    rs.getInt("UserID"),
                    rs.getString("UserName"),
                    rs.getString("UserType"),
                    rs.getInt("TelegramUserID")
            ));
        }
        return users;
    }
}
