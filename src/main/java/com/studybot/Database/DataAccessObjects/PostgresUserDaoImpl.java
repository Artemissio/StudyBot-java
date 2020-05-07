package com.studybot.Database.DataAccessObjects;

import com.studybot.Database.DataTransferObjects.User;
import com.studybot.Exceptions.ConfigurationException;
import com.studybot.Exceptions.DatabaseException;

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
            throw new DatabaseException("Couldn't Get User By UserID", e);
        }
        return user;
    }

    @Override
    public User getUserByTelegramUserIdAndType(int telegramUserId, String userType) throws DatabaseException {
        User user = null;
        try(Connection conn = databaseConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ? AND actor_type = ?")){
            stmt.setInt(1, telegramUserId);
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
            throw new DatabaseException("Couldn't Get User By TelegramUserID And Type", e);
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
            throw new DatabaseException("Couldn't Get Users By Type", e);
        }
        return users;
    }
}
