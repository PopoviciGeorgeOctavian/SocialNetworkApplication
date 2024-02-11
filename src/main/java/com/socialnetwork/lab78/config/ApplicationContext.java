package com.socialnetwork.lab78.config;

import java.util.Properties;

/**
 * The ApplicationContext class represents the context of the application
 * and provides access to configuration properties.
 */
public class ApplicationContext {

    // The properties loaded from the configuration file
    private static final Properties PROPERTIES = Config.getProperties();

    /**
     * Gets the configuration properties for the application.
     * @return The properties loaded from the configuration file.
     */
    public static Properties getPROPERTIES() {
        return PROPERTIES;
    }
}