package com.studybot.db.dao;

import com.studybot.db.DatabaseConnectionFactory;
import com.studybot.exceptions.ConfigurationException;
import com.studybot.exceptions.DatabaseException;

public class PostgresAbstractDaoImpl {

    protected DatabaseConnectionFactory databaseConnectionFactory;

    public PostgresAbstractDaoImpl() throws DatabaseException, ConfigurationException {
        this.databaseConnectionFactory = DatabaseConnectionFactory.getInstance();
    }
}
