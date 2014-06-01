package com.istweet.twitter.catcher.mysql;

import com.istweet.twitter.catcher.entity.TwitterEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Local MySQL access
 *
 * @author Onur
 */
@Singleton
public class MySQLAccess {

    private static final String propertyFile = "catcher.properties";
    /**
     * SQL connection
     */
    Connection connection = null;
    /**
     * Properties
     */
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

        try {
            System.out.println("Connecting database...");
            connection = DriverManager.getConnection(
                    props.getProperty("mysqlURL"),
                    props.getProperty("mysqlUser"),
                    props.getProperty("mysqlPW"));

            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }

    }

    /**
     * Get tweets from database
     *
     * @param propertyKey
     * @return
     * @throws SQLException
     */
    public List<TwitterEntity> getTweetsFromDB(String propertyKey)
            throws SQLException {
        List<TwitterEntity> allTweets = new ArrayList<TwitterEntity>();
        String sql = props.getProperty(propertyKey);
        java.sql.PreparedStatement preparedStatement = connection
                .prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            TwitterEntity twitterEntity = new TwitterEntity();
            twitterEntity.setId(resultSet.getString("id"));
            twitterEntity.setCreated_at((new Date(resultSet.getDate(
                    "created_at").getTime())));
            twitterEntity.setText((resultSet.getString("text")));
            List<String> urlEntities = new ArrayList<String>();
            if ((resultSet.getString("url_1")) != null) {
                urlEntities.add(resultSet.getString("url_1"));
            }
            if ((resultSet.getString("url_2")) != null) {
                urlEntities.add(resultSet.getString("url_2"));
            }
            if ((resultSet.getString("url_3")) != null) {
                urlEntities.add(resultSet.getString("url_3"));
            }
            twitterEntity.setUrls(urlEntities);

            twitterEntity.setGeo_location_latitude(resultSet
                    .getString("geo_location_latitude"));
            twitterEntity.setGeo_location_longitude(resultSet
                    .getString("geo_location_longitude"));
            twitterEntity.setPlace(resultSet.getString("place"));

            twitterEntity.setUser(resultSet.getString("user"));
            twitterEntity.setUser_follower_count(resultSet
                    .getInt("user_follower_count"));
            twitterEntity
                    .setIn_reply_name(resultSet.getString("in_reply_name"));
            twitterEntity.setIn_reply_status(resultSet
                    .getString("in_reply_status"));

            twitterEntity.setRetweet_count(resultSet.getInt("retweet_count"));
            twitterEntity.setFavorite_count(resultSet.getInt("favorite_count"));

            allTweets.add(twitterEntity);
        }
        return allTweets;

    }

    /**
     * Execute query on DB
     *
     * @param query
     * @throws SQLException
     */
    public void executeQuery(String query) throws SQLException {

        java.sql.PreparedStatement preparedStatement = connection
                .prepareStatement(query);
        preparedStatement.executeUpdate();
    }

    /**
     * Insert tweet into database
     *
     * @param status
     * @throws Exception
     */
    public void insertTweetToDatabase(Status status) throws Exception {

        String sqlCheck = "SELECT id FROM twittercatcher.jobtweets WHERE id=?";
        java.sql.PreparedStatement preparedStatementForCheck = connection
                .prepareStatement(sqlCheck);
        preparedStatementForCheck.setString(1, String.valueOf(status.getId()));
        ResultSet resultSet = preparedStatementForCheck.executeQuery();
        if (!resultSet.next()) {

            String sql = "INSERT INTO  twittercatcher.jobtweets ( "
                    + "id , "
                    + "created_at , "
                    + "text , "
                    + "url_1 ,  "
                    + "url_2 , "
                    + "url_3 , "
                    +

                    "geo_location_latitude , "
                    + "geo_location_longitude , "
                    + "place , "
                    + "user , "
                    + "user_follower_count , "
                    + "in_reply_name , "
                    + "in_reply_status , "
                    + "retweet_count , "
                    + "favorite_count) "
                    + "VALUES ( ?,  ?,  ?, ? , ? , ? ,  ? , ? , ? , ?, ?, ? , ? , ?, ?)";

            java.sql.PreparedStatement preparedStatement = connection
                    .prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(status.getId()));
            preparedStatement.setDate(2, new java.sql.Date(status
                    .getCreatedAt().getTime()));
            preparedStatement.setString(3, status.getText());

            URLEntity[] entities = status.getURLEntities();
            if (entities != null) {
                for (int i = 0; i < 3; i++) {
                    if (entities.length > i) {
                        preparedStatement
                                .setString(4 + i, entities[i].getURL());
                    } else {
                        preparedStatement.setString(4 + i, null);
                    }
                }
            } else {
                preparedStatement.setString(4, null);
                preparedStatement.setString(5, null);
                preparedStatement.setString(6, null);
            }

            if (status.getGeoLocation() != null) {
                preparedStatement.setString(7,
                        String.valueOf(status.getGeoLocation().getLatitude()));
                preparedStatement.setString(8,
                        String.valueOf(status.getGeoLocation().getLongitude()));

            } else {
                preparedStatement.setString(7, null);
                preparedStatement.setString(8, null);
            }
            if (status.getPlace() != null) {
                preparedStatement.setString(9, status.getPlace().getFullName());
            } else {
                preparedStatement.setString(9, null);

            }

            preparedStatement.setString(10, status.getUser().getScreenName());
            preparedStatement.setInt(11, status.getUser().getFollowersCount());
            preparedStatement.setString(12, status.getInReplyToScreenName());
            preparedStatement.setString(13,
                    String.valueOf(status.getInReplyToStatusId()));
            preparedStatement.setInt(14, status.getRetweetCount());
            preparedStatement.setInt(15, status.getFavoriteCount());
            preparedStatement.executeUpdate();

            System.out.println(preparedStatement.toString());
        }

    }

}