package com.studybot.db.dao;

import com.studybot.db.dto.Trigger;
import com.studybot.exceptions.ConfigurationException;
import com.studybot.exceptions.DatabaseException;

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
            throw new DatabaseException("Couldn't retrieve triggers", e);
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
            throw new DatabaseException("Couldn't retrieve triggers", e);
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
            throw new DatabaseException("Couldn't retrieve triggers", e);
        }
        return triggers;
    }

    private Set<Trigger> processMultipleResultSet(ResultSet rs) throws SQLException {
        Set<Trigger> triggers = new HashSet<>();
        while (rs.next()) {
            Trigger trigger = new Trigger(
                    rs.getInt("trigger_id"),
                    rs.getString("trigger_type"),
                    rs.getString("trigger_subtype"),
                    rs.getString("bot_token"),
                    rs.getString("trigger_value")
            );
            triggers.add(trigger);
        }
        return triggers;
    }
}
