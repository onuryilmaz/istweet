package com.istweet.twitter.catcher.schedule;

import com.istweet.twitter.catcher.entity.TwitterEntity;
import com.istweet.twitter.catcher.mysql.MySQLAccess;
import com.istweet.twitter.catcher.mysql.RemoteMySQLAccess;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * Scheduled copier class
 *
 * @author Onur
 */
@Singleton
public class ScheduledCopier {

    /** Remote database access */
    RemoteMySQLAccess remoteDatabaseAccess;

    /** Local database access */
    MySQLAccess localDatabaseAccess;

    /** MySQL connection */
    Connection connection = null;

    /** Add query string */
    String addQuery;

    /** Remove query string */
    String removeQuery;

    /** Properties */
    private static final String propertyFile = "catcher.properties";
    Properties props = null;


    /**
     * Initialize remote and local database connections
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

        remoteDatabaseAccess = new RemoteMySQLAccess();
        localDatabaseAccess = new MySQLAccess();

        localDatabaseAccess.initialize();
        remoteDatabaseAccess.initialize();

    }

    /**
     * Periodic copier method
     *
     * @throws Exception
     */
    @Schedule(minute = "*/29", hour = "*")
    public void copy() throws Exception {
        System.out.println("Copier");
        List<TwitterEntity> twitterEntities = localDatabaseAccess
                .getTweetsFromDB("processedtweets.select");
        int i = 0;
        for (TwitterEntity twitterEntity : twitterEntities) {
            remoteDatabaseAccess.insertTweetToDatabase(twitterEntity);
            System.out.println(i);
            i++;
        }

        remoteDatabaseAccess
                .executeQuery("UPDATE processedtweets SET  popularity= 10 * LOG10((favorite_count + 1) * (retweet_count + 1) * (user_follower_count + 1)) WHERE fb_unrelated < 1");

        System.out.println("Copier Ended");
    }
}