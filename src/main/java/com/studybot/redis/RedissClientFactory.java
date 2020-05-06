package com.studybot.redis;

import com.studybot.exceptions.ConfigurationException;
import com.studybot.utils.PropertiesHandler;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Properties;

public final class RedissClientFactory {

    private RedissonClient client;
    private static RedissClientFactory redissClientFactory;

    private RedissClientFactory() throws ConfigurationException {
        Properties properties = PropertiesHandler.getProperties("redis.properties");
        Config config = new Config();
        config.useSingleServer().setAddress(properties.getProperty("REDIS_ADDRESS"));
        client = Redisson.create(config);
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
