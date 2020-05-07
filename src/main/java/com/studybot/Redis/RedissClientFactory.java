package com.studybot.Redis;

import com.studybot.Exceptions.ConfigurationException;
import com.studybot.Exceptions.ServiceConnectionException;
import com.studybot.Handlers.PropertiesHandler;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Properties;

public final class RedissClientFactory {

    private RedissonClient client;
    private static RedissClientFactory redissClientFactory;

    private RedissClientFactory() throws ConfigurationException {
        try{
            Properties properties = PropertiesHandler.getProperties("redis.properties");
            Config config = new Config();
            config.useSingleServer().setAddress(properties.getProperty("REDIS_ADDRESS"));
            client = Redisson.create(config);
        }
        catch (Exception e){
            throw new ConfigurationException("Can`t Connect To Redis", e);
        }
    }

    public static RedissClientFactory getInstance() throws ConfigurationException {
        if (redissClientFactory == null) {
            redissClientFactory = new RedissClientFactory();
        }
        return redissClientFactory;
    }

    public RedissonClient getRedissClient(){
        return client;
    }
}
