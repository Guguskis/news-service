package lt.liutikas.reddit.client;

import lt.liutikas.reddit.model.twitter.Tweet;

import java.util.List;

public interface TwitterClient {

    List<Tweet> getTweetsByKeyword(String keyword);

    List<Tweet> getTweetsByUser(String user);

}
