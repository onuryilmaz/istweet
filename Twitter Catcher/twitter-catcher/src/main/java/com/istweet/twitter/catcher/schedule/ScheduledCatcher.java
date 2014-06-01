package com.istweet.twitter.catcher.schedule;

import com.istweet.twitter.catcher.mysql.MySQLAccess;
import twitter4j.*;

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
 * Scheduled catcher class
 *
 * @author Onur
 */
@Singleton
public class ScheduledCatcher {

    /** Database access */
    MySQLAccess databaseAccess;

    /** Twitter API instance */
    Twitter twitter;

    Connection connection = null;

    /** Connection */
    Query query;

    /** ID of the last tweet */
    long lastTweetId = 0;

    /** Property file */
    private static final String propertyFile = "catcher.properties";
    Properties props = null;

    /**
     * Initialize connection
     *
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
        twitter = TwitterFactory.getSingleton();
        query = new Query(props.getProperty("searchQuery"));
        query.setSinceId(lastTweetId);

    }

    /**
     * Search and add to database periodically
     * @throws Exception
     */
    @AccessTimeout(value = 20, unit = TimeUnit.MINUTES)
    @Schedule(minute = "*/25", hour = "*")
    public void searchAndAddToDB() throws Exception {
        System.out.println("Search and add raw");
        databaseAccess.initialize();
        query.setSinceId(lastTweetId);
        QueryResult result = twitter.search(query);

        for (Status status : result.getTweets()) {
            databaseAccess.insertTweetToDatabase(status);
            lastTweetId = status.getId();
        }

        while (result.nextQuery() != null) {
            Query query = result.nextQuery();
            result = twitter.search(query);
            for (Status status : result.getTweets()) {

                databaseAccess.insertTweetToDatabase(status);
                lastTweetId = status.getId();
            }
        }

    }

}