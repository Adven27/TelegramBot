package org.telegram.services;

import twitter4j.*;

/**
 * Created by k1per on 24.02.2017.
 */
public class TwitterServiceImpl implements TwitterService {

    Twitter twitter = TwitterFactory.getSingleton();

    @Override
    public String getLatestTweet(String userName) {
        try {
            Query query = new Query("from:" + userName);
            QueryResult result = twitter.search(query);
            return result.getTweets().get(0).getText();
        }
        catch (TwitterException ex){
            return "No twits for you bro :(";
        }
    }
}
