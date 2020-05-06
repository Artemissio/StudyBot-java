package com.studybot.db.dao;

import com.studybot.db.dto.UserChat;
import com.studybot.exceptions.ConfigurationException;
import com.studybot.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PostgresUserChatDaoImpl extends PostgresAbstractDaoImpl implements UserChatDao {

    public PostgresUserChatDaoImpl() throws DatabaseException, ConfigurationException {
    }

    @Override
    public UserChat getUserChat(int userId, long chatId) throws DatabaseException {
        UserChat userChat = null;
        try(Connection conn = databaseConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM UserChats WHERE UserID = ? AND ChatID = ?")){
            stmt.setInt(1, userId);
            stmt.setLong(2, chatId);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    userChat = new UserChat(
                            rs.getInt("UserID"),
                            rs.getLong("ChatID")
                    );
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Couldn't retrieve userChat with such user and chat id", e);
        }
        return userChat;
    }

    @Override
    public void addUserChat(UserChat userChat) throws DatabaseException {
        try(Connection conn = databaseConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO UserChats(UserID, ChatID) VALUES (?, ?)")){
            stmt.setInt(1, userChat.getUserId());
            stmt.setLong(2, userChat.getChatId());
            stmt.executeUpdate();
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Couldn't insert such userChat", e);
        }
    }

    @Override
    public Set<UserChat> getUserChatsByType(String type) throws DatabaseException {
        Set<UserChat> userChats = new HashSet<>();
        try(Connection conn = databaseConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM UserChats WHERE UserID in (SELECT UserID FROM Users WHERE UserType = ?)")){
            stmt.setString(1, type);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    userChats.add(new UserChat(
                            rs.getInt("UserID"),
                            rs.getLong("ChatID")
                    ));
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Couldn't retrieve triggers", e);
        }
        return userChats;
    }

    private UserChat processSingleResultSet(ResultSet rs) throws SQLException {
        UserChat userChat = null;
        while (rs.next()) {
            userChat = new UserChat(
                    rs.getInt("UserID"),
                    rs.getLong("ChatID")
            );
        }
        return userChat;
    }

    private Set<UserChat> processMultipleResultSet(ResultSet rs) throws SQLException {
        Set<UserChat> userChats = new HashSet<>();
        while (rs.next()) {
            userChats.add(new UserChat(
                    rs.getInt("UserID"),
                    rs.getLong("ChatID")
            ));
        }
        return userChats;
    }
}
