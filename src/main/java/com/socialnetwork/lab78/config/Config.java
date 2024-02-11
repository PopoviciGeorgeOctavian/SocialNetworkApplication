package com.socialnetwork.lab78.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * The `Config` class provides methods to retrieve configuration properties for the social network application.
 */
public class Config {

    /**
     * The location of the configuration file.
     * It is set to the path of the "config.properties" file within the classpath.
     */
    public static String CONFIG_LOCATION = Config.class.getClassLoader().getResource("config.properties").getFile();

    /**
     * Retrieves the configuration properties from the specified file.
     *
     * @return A `Properties` object containing the configuration properties.
     * @throws RuntimeException if there is an issue loading the configuration properties.
     */
    public static Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(CONFIG_LOCATION));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
