package com.studybot.Database;

import com.studybot.Exceptions.ConfigurationException;
import com.studybot.Exceptions.DatabaseException;
import com.studybot.Handlers.PropertiesHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnectionFactory {

    private Properties props;
    private static DatabaseConnectionFactory databaseConnectionFactory;

    private DatabaseConnectionFactory() throws DatabaseException, ConfigurationException {
        props = PropertiesHandler.getProperties("db.properties");
        try{
            Class.forName(props.getProperty("DB_DRIVER_CLASS"));
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Couldn't Load DB Driver Class", e);
        }
    }

    public static DatabaseConnectionFactory getInstance() throws DatabaseException, ConfigurationException {
        if (databaseConnectionFactory == null) {
            databaseConnectionFactory = new DatabaseConnectionFactory();
        }
        return databaseConnectionFactory;
    }

    public Connection getConnection() throws DatabaseException {
        Connection con;
        try {
            con = DriverManager.getConnection(
                    props.getProperty("DB_URL"),
                    props.getProperty("DB_USERNAME"),
                    props.getProperty("DB_PASSWORD"));
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't Connect To The Database", e);
        }
        return con;
    }
}
