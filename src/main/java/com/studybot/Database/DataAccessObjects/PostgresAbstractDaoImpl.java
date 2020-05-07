package com.studybot.Database.DataAccessObjects;

import com.studybot.Database.DatabaseConnectionFactory;
import com.studybot.Exceptions.ConfigurationException;
import com.studybot.Exceptions.DatabaseException;

public class PostgresAbstractDaoImpl {

    protected DatabaseConnectionFactory databaseConnectionFactory;

    public PostgresAbstractDaoImpl() throws DatabaseException, ConfigurationException {
        this.databaseConnectionFactory = DatabaseConnectionFactory.getInstance();
    }
}
