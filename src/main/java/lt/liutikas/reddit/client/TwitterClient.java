package lt.liutikas.reddit.client;

import lt.liutikas.reddit.model.twitter.Tweet;

import java.util.List;

public interface TwitterClient {

    List<Tweet> getTweets(String keyword);

}
