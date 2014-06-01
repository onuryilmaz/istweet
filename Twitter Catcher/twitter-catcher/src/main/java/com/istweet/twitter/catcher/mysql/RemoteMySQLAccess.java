package com.istweet.twitter.catcher.mysql;

import com.istweet.twitter.catcher.entity.TwitterEntity;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Remote MySQL Access
 *
 * @author Onur
 */
@Singleton
public class RemoteMySQLAccess {

    /**
     * Properties
     */
    private static final String propertyFile = "catcher.properties";
    /**
     * SQL connection
     */
    Connection connection = null;
    Properties props = null;

    /**
     * Initialise MySQL connection
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
                    props.getProperty("remoteMysqlURL"),
                    props.getProperty("remoteMysqlUser"),
                    props.getProperty("remoteMysqlPW"));

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
     * Execute query on database
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
     * Insert tweets to database
     *
     * @param entity
     * @throws Exception
     */
    public void insertTweetToDatabase(TwitterEntity entity) throws Exception {

        String sqlCheck = "SELECT id FROM twittercatcher.processedtweets WHERE id=?";
        java.sql.PreparedStatement preparedStatementForCheck = connection
                .prepareStatement(sqlCheck);
        preparedStatementForCheck.setString(1, entity.getId());
        ResultSet resultSet = preparedStatementForCheck.executeQuery();
        if (!resultSet.next()) {

            String sql = "INSERT INTO  twittercatcher.processedtweets ( "
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

            preparedStatement.setString(1, String.valueOf(entity.getId()));

            preparedStatement.setDate(2, (Date) entity.getCreated_at());
            preparedStatement.setString(3, entity.getText());

            List<String> urls = entity.getUrls();

            if (urls.size() == 0) {
                preparedStatement.setString(4, null);
                preparedStatement.setString(5, null);
                preparedStatement.setString(6, null);
            }

            if (urls.size() == 1) {
                preparedStatement.setString(4, urls.get(0));
                preparedStatement.setString(5, null);
                preparedStatement.setString(6, null);
            }

            if (urls.size() == 2) {
                preparedStatement.setString(4, urls.get(0));
                preparedStatement.setString(5, urls.get(1));
                preparedStatement.setString(6, null);
            }

            if (urls.size() == 3) {
                preparedStatement.setString(4, urls.get(0));
                preparedStatement.setString(5, urls.get(1));
                preparedStatement.setString(6, urls.get(2));
            }

            preparedStatement.setString(7, entity.getGeo_location_latitude());
            preparedStatement.setString(8, entity.getGeo_location_longitude());
            preparedStatement.setString(9, entity.getPlace());

            preparedStatement.setString(10, entity.getUser());
            preparedStatement.setInt(11, entity.getUser_follower_count());
            preparedStatement.setString(12, entity.getIn_reply_name());
            preparedStatement.setString(13, entity.getIn_reply_status());
            preparedStatement.setInt(14, entity.getRetweet_count());
            preparedStatement.setInt(15, entity.getFavorite_count());
            preparedStatement.executeUpdate();

            System.out.println(preparedStatement.toString());
        }

    }
}