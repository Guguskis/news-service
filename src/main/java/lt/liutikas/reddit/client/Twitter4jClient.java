package lt.liutikas.reddit.client;

import lt.liutikas.reddit.assembler.TwitterAssembler;
import lt.liutikas.reddit.model.twitter.Tweet;
import lt.liutikas.reddit.model.twitter.TweetSubChannelType;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.util.List;
import java.util.function.Function;
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
    public List<Tweet> getTweets(String keyword) {
        try {
            return getTweetsByKeyword(keyword);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Tweet> getTweetsByKeyword(String keyword) throws TwitterException {
        Query query = getQuery(keyword);
        QueryResult search = twitter.search(query);
        return search.getTweets().stream()
                .map(assembleTweetByKeyword(keyword))
                .collect(Collectors.toList());
    }

    private Function<Status, Tweet> assembleTweetByKeyword(String keyword) {
        return (Status status) -> {
            Tweet tweet = twitterAssembler.assembleTweet(status);
            tweet.setKeyword(keyword);
            tweet.setSubChannelType(TweetSubChannelType.KEYWORD);
            return tweet;
        };
    }

    private Query getQuery(String keyword) {
        Query query = new Query(keyword + " -filter:retweets");
        query.lang("en");
        query.setCount(20);
        query.setResultType(Query.ResultType.recent);

        return query;
    }

}
