package com.studybot.RabbitMQ;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.studybot.Exceptions.ConfigurationException;
import com.studybot.Exceptions.ServiceConnectionException;
import com.studybot.Handlers.PropertiesHandler;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public final class RabbitMqConnectionFactory {

    private Properties props;
    private ConnectionFactory connectionFactory;
    private static RabbitMqConnectionFactory rabbitMqConnectionFactory;

    private RabbitMqConnectionFactory() throws ConfigurationException {
        props = PropertiesHandler.getProperties("rabbitmq.properties");
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(props.getProperty("RABBIT_MQ_HOST"));
        connectionFactory.setPort(Integer.parseInt(props.getProperty("RABBIT_MQ_PORT")));
    }

    public static RabbitMqConnectionFactory getInstance() throws ConfigurationException {
        if (rabbitMqConnectionFactory == null) {
            rabbitMqConnectionFactory = new RabbitMqConnectionFactory();
        }
        return rabbitMqConnectionFactory;
    }

    public Connection getConnection() throws ServiceConnectionException {
        try {
            return connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new ServiceConnectionException("Can't Connect To RabbitMQ", e);
        }
    }
}
