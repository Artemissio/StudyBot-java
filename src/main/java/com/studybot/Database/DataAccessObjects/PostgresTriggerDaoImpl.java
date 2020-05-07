package com.studybot.Database.DataAccessObjects;

import com.studybot.Database.DataTransferObjects.Trigger;
import com.studybot.Exceptions.ConfigurationException;
import com.studybot.Exceptions.DatabaseException;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PostgresTriggerDaoImpl extends PostgresAbstractDaoImpl implements TriggerDao {

    public PostgresTriggerDaoImpl() throws DatabaseException, ConfigurationException {
    }

    @Override
    public Set<Trigger> getAllTriggers() throws DatabaseException {
        Set<Trigger> triggers = new HashSet<>();
        try(Connection conn = databaseConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Triggers")){
            while (rs.next()) {
                triggers.add(new Trigger(
                        rs.getInt("TriggerID"),
                        rs.getString("TriggerType"),
                        rs.getString("TriggerSubtype"),
                        rs.getString("BotToken"),
                        rs.getString("TriggerValue")
                ));
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Couldn't Get All Triggers", e);
        }
        return triggers;
    }

    @Override
    public Set<Trigger> getCommonTriggers() throws DatabaseException {
        Set<Trigger> triggers = new HashSet<>();
        try(Connection conn = databaseConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Triggers WHERE TriggerType = ?")){
            stmt.setString(1, "common");
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    triggers.add(new Trigger(
                            rs.getInt("TriggerID"),
                            rs.getString("TriggerType"),
                            rs.getString("TriggerSubtype"),
                            rs.getString("BotToken"),
                            rs.getString("TriggerValue")
                    ));
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Couldn't Get Common Triggers", e);
        }
        return triggers;
    }

    @Override
    public Set<Trigger> getBotTriggers(String token) throws DatabaseException {
        Set<Trigger> triggers = new HashSet<>();
        try(Connection conn = databaseConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Triggers WHERE BotToken = ?")){
            stmt.setString(1, token);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    triggers.add(new Trigger(
                            rs.getInt("TriggerID"),
                            rs.getString("TriggerType"),
                            rs.getString("TriggerSubtype"),
                            rs.getString("BotToken"),
                            rs.getString("TriggerValue")
                    ));
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Couldn't Get Bot Triggers", e);
        }
        return triggers;
    }
}
