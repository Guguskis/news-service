package lt.liutikas.reddit.client;

import lt.liutikas.reddit.assembler.TwitterAssembler;
import lt.liutikas.reddit.domain.entity.twitter.Tweet;
import lt.liutikas.reddit.domain.entity.twitter.TweetSubChannelType;
import org.springframework.stereotype.Component;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class Twitter4jClient implements TwitterClient {

    private final Twitter twitter;
    private final TwitterAssembler twitterAssembler;

    public Twitter4jClient(Twitter twitter, TwitterAssembler twitterAssembler) {
        this.twitter = twitter;
        this.twitterAssembler = twitterAssembler;
    }

    @Override
    public List<Tweet> getTweetsByKeyword(String keyword) {
        try {
            Query query = getKeywordQuery(keyword);
            List<Tweet> tweets = searchTweets(query);
            tweets.forEach(setKeywordSubChannel(keyword));
            return tweets;
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Tweet> getTweetsByUser(String user) {
        try {
            Query query = getUserQuery(user);
            List<Tweet> tweets = searchTweets(query);
            tweets.forEach(setUserSubChannel(user));
            return tweets;
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Tweet> searchTweets(Query query) throws TwitterException {
        QueryResult search = twitter.search(query);
        return search.getTweets().stream()
                .map(twitterAssembler::assembleTweet)
                .collect(Collectors.toList());
    }

    private Consumer<Tweet> setUserSubChannel(String user) {
        return tweet -> {
            tweet.setSubChannelType(TweetSubChannelType.USER);
            tweet.setUser(user);
        };
    }

    private Consumer<? super Tweet> setKeywordSubChannel(String keyword) {
        return tweet -> {
            tweet.setSubChannelType(TweetSubChannelType.KEYWORD);
            tweet.setKeyword(keyword);
        };
    }

    private Query getKeywordQuery(String keyword) {
        String queryString = String.format("%s -is:retweets", keyword);
        return getQuery(queryString);
    }

    private Query getUserQuery(String user) {
        String queryString = String.format("@%s -is:retweets", user);
        return getQuery(queryString);
    }

    private Query getQuery(String queryString) {
        Query query = new Query(queryString);

        query.lang("en");
        query.setCount(20);
        query.setResultType(Query.ResultType.recent);

        return query;
    }

}
