package com.istweet.twitter.catcher.schedule;

import com.istweet.twitter.catcher.mysql.MySQLAccess;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled maintanence class
 *
 * @author Onur
 */
@Singleton
public class ScheduledMaintanence {

    /**
     * Property file
     */
    private static final String propertyFile = "catcher.properties";
    /**
     * Database access
     */
    MySQLAccess databaseAccess;
    /**
     * Database connection
     */
    Connection connection = null;
    Properties props = null;

    String addQuery;
    String removeQuery;

    /**
     * Initialize connections from property file
     * @throws IOException
     */
    @PostConstruct
    public void initialize() throws IOException {
        String resourceName = propertyFile;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props = new Properties();
        try (InputStream resourceStream = loader
                .getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        }

        databaseAccess = new MySQLAccess();
        addQuery = props.getProperty("maintanence.add");
        removeQuery = props.getProperty("maintanence.remove");

    }

    /**
     * Search and add to database periodically
     * @throws Exception
     */
    @AccessTimeout(value = 20, unit = TimeUnit.MINUTES)
    @Schedule(minute = "*/27", hour = "*")
    public void searchAndAddToDB() throws Exception {
        System.out.println("Maintanence: search and add");
        databaseAccess.initialize();
        databaseAccess.executeQuery(addQuery);

    }

    /**
     * Remove duplicates from database periodically
     * @throws Exception
     */
    @Schedule(minute = "*/28", hour = "*")
    public void removeDuplicates() throws Exception {
        System.out.println("Maintanence: remove duplicates");
        databaseAccess.initialize();
        databaseAccess.executeQuery(removeQuery);

    }

}