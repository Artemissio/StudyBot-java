package com.studybot.Handlers;

import com.studybot.Exceptions.ConfigurationException;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesHandler {

    public static Properties getProperties(String path) throws ConfigurationException {
        Properties props = new Properties();
        try{
            props.load(PropertiesHandler.class.getResourceAsStream("/" + path));
        } catch (IOException e) {
            throw new ConfigurationException("Couldn't Get Configuration Properties", e);
        }
        return props;
    }
}
