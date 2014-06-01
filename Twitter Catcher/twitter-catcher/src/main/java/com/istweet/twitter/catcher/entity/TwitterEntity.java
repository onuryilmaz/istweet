package com.istweet.twitter.catcher.entity;

import java.util.Date;
import java.util.List;

/**
 * Class for holding Tweet related data
 *
 * @author Onur
 */
public class TwitterEntity {

    /**
     * ID of the tweet
     */
    String id;

    /**
     * Creation date of the tweet
     */
    Date created_at;

    /**
     * Text of the tweet
     */
    String text;

    /**
     * List of the URLs of the tweet
     */
    List<String> urls;

    /**
     * Latitute of the tweet
     */
    String geo_location_latitude;

    /**
     * Longitude of the tweet
     */
    String geo_location_longitude;

    /**
     * Place of the tweet
     */
    String place;

    /**
     * User of the tweet
     */
    String user;

    /**
     * Follower count of the user of the tweet
     */
    int user_follower_count;

    /**
     * In-reply name of the tweet
     */
    String in_reply_name;

    /**
     * In-reply status ID of the tweet
     */
    String in_reply_status;

    /**
     * Retweet count of the tweet
     */
    int retweet_count;

    /**
     * Favorite count of the tweet
     */
    int favorite_count;

    /**
     * Constructor using fields
     *
     * @param id
     * @param created_at
     * @param text
     * @param urls
     * @param geo_location_latitude
     * @param geo_location_longitude
     * @param place
     * @param user
     * @param user_follower_count
     * @param in_reply_name
     * @param in_reply_status
     * @param retweet_count
     * @param favorite_count
     */
    public TwitterEntity(String id, Date created_at, String text,
                         List<String> urls, String geo_location_latitude,
                         String geo_location_longitude, String place, String user,
                         int user_follower_count, String in_reply_name,
                         String in_reply_status, int retweet_count, int favorite_count) {
        super();
        this.id = id;
        this.created_at = created_at;
        this.text = text;
        this.urls = urls;
        this.geo_location_latitude = geo_location_latitude;
        this.geo_location_longitude = geo_location_longitude;
        this.place = place;
        this.user = user;
        this.user_follower_count = user_follower_count;
        this.in_reply_name = in_reply_name;
        this.in_reply_status = in_reply_status;
        this.retweet_count = retweet_count;
        this.favorite_count = favorite_count;
    }

    /**
     * Default constructor
     */
    public TwitterEntity() {
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the created_at
     */
    public Date getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at the created_at to set
     */
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the urls
     */
    public List<String> getUrls() {
        return urls;
    }

    /**
     * @param urls the urls to set
     */
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    /**
     * @return the geo_location_latitude
     */
    public String getGeo_location_latitude() {
        return geo_location_latitude;
    }

    /**
     * @param geo_location_latitude the geo_location_latitude to set
     */
    public void setGeo_location_latitude(String geo_location_latitude) {
        this.geo_location_latitude = geo_location_latitude;
    }

    /**
     * @return the geo_location_longitude
     */
    public String getGeo_location_longitude() {
        return geo_location_longitude;
    }

    /**
     * @param geo_location_longitude the geo_location_longitude to set
     */
    public void setGeo_location_longitude(String geo_location_longitude) {
        this.geo_location_longitude = geo_location_longitude;
    }

    /**
     * @return the place
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place the place to set
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the user_follower_count
     */
    public int getUser_follower_count() {
        return user_follower_count;
    }

    /**
     * @param user_follower_count the user_follower_count to set
     */
    public void setUser_follower_count(int user_follower_count) {
        this.user_follower_count = user_follower_count;
    }

    /**
     * @return the in_reply_name
     */
    public String getIn_reply_name() {
        return in_reply_name;
    }

    /**
     * @param in_reply_name the in_reply_name to set
     */
    public void setIn_reply_name(String in_reply_name) {
        this.in_reply_name = in_reply_name;
    }

    /**
     * @return the in_reply_status
     */
    public String getIn_reply_status() {
        return in_reply_status;
    }

    /**
     * @param in_reply_status the in_reply_status to set
     */
    public void setIn_reply_status(String in_reply_status) {
        this.in_reply_status = in_reply_status;
    }

    /**
     * @return the retweet_count
     */
    public int getRetweet_count() {
        return retweet_count;
    }

    /**
     * @param retweet_count the retweet_count to set
     */
    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    /**
     * @return the favorite_count
     */
    public int getFavorite_count() {
        return favorite_count;
    }

    /**
     * @param favorite_count the favorite_count to set
     */
    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

}
