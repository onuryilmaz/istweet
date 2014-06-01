package com.istweet.twitter.catcher;

import com.istweet.twitter.catcher.mysql.RemoteMySQLAccess;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for gathering user feedback
 *
 * @author Onur
 */
@Path("/feedback")
public class FeedbackGatherer {

    /**
     * Remote access
     */
    RemoteMySQLAccess mySQLAccess;

    /**
     * Post constructor with initialization
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        mySQLAccess = new RemoteMySQLAccess();
        mySQLAccess.initialize();
    }

    /**
     * Get user feedback
     *
     * @param id
     * @return
     */
    @GET
    @Produces("text/html; charset=UTF-8")
    @Path("/unrelated/{id}")
    public String unrelatedTweet(@PathParam("id") String id) {

        String updateUnrelatedCount = "UPDATE processedtweets SET fb_unrelated=fb_unrelated+1 WHERE id="
                + id;
        try {
            mySQLAccess.executeQuery(updateUnrelatedCount);
        } catch (SQLException e) {

        }

        String updatePopularity = "UPDATE processedtweets SET popularity=popularity/2 WHERE fb_unrelated = 1 AND id="
                + id;
        try {
            mySQLAccess.executeQuery(updatePopularity);
        } catch (SQLException e) {

        }
        return id;
    }

}
