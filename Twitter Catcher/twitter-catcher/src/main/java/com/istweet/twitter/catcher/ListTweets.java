package com.istweet.twitter.catcher;

import com.istweet.twitter.catcher.entity.TwitterEntity;
import com.istweet.twitter.catcher.mysql.MySQLAccess;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * List all tweets in the database
 *
 * @author Onur
 */
@Path("/db")
public class ListTweets {

    /**
     * Access to DB
     */
    MySQLAccess mySQLAccess;

    /**
     * Post constructor
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        mySQLAccess = new MySQLAccess();
        mySQLAccess.initialize();
    }

    /**
     * Get raw tweets from database
     *
     * @return
     */
    @GET
    @Produces("text/html; charset=UTF-8")
    @Path("/raw/all")
    public String getRawTweets() {

        List<TwitterEntity> list = new ArrayList<TwitterEntity>();

        String table = "<html><head><meta http-equiv=content-type content=text/html;charset=iso-8859-9></head> <table>";

        try {
            list = mySQLAccess.getTweetsFromDB("jobtweets.select");
        } catch (SQLException e) {

            e.printStackTrace();
        }

        for (TwitterEntity entity : list) {
            table = table + "<tr>" + "<td> <strong>" + entity.getUser()
                    + ":</strong></td>" + "<td>" + entity.getText() + "</td>"
                    + "</tr>";
        }
        table = table + "</table> </html>";

        return table;
    }

    /**
     * Get tweet count
     *
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/raw/count")
    public String getTweetCount() {

        List<TwitterEntity> list = new ArrayList<TwitterEntity>();

        try {
            list = mySQLAccess.getTweetsFromDB("jobtweets.select");
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return String.valueOf(list.size());
    }

    /**
     * Get processed tweets from database
     *
     * @return
     */
    @GET
    @Produces("text/html; charset=UTF-8")
    @Path("/processed/all")
    public String getProcessedTweets() {

        List<TwitterEntity> list = new ArrayList<TwitterEntity>();

        String table = "<html><head><meta http-equiv=content-type content=text/html;charset=iso-8859-9></head> <table>";

        try {
            list = mySQLAccess.getTweetsFromDB("processedtweets.select");
        } catch (SQLException e) {

            e.printStackTrace();
        }

        for (TwitterEntity entity : list) {
            table = table + "<tr>" + "<td> <strong>" + entity.getUser()
                    + ":</strong></td>" + "<td>" + entity.getText() + "</td>"
                    + "</tr>";
        }
        table = table + "</table> </html>";

        return table;
    }

    /**
     * Get processed tweet count from database
     *
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/processed/count")
    public String getProcessedTweetCount() {

        List<TwitterEntity> list = new ArrayList<TwitterEntity>();

        try {
            list = mySQLAccess.getTweetsFromDB("processedtweets.select");
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return String.valueOf(list.size());
    }

}
