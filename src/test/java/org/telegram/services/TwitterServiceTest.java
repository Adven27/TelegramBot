package org.telegram.services;

import org.junit.Test;
import twitter4j.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * Created by k1per on 24.02.2017.
 */
public class TwitterServiceTest {

    @Test
    public void shouldConnectToTwitter() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query("from:besseifunction");
        QueryResult result = twitter.search(query);
        assertNotNull(result);
    }
}
