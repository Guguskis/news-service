package lt.liutikas.reddit.source;

import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.client.TwitterClient;
import lt.liutikas.reddit.config.properties.ScanProperties;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.twitter.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TwitterNewsSource implements NewsSource {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterClient.class);

    private final TwitterClient twitterClient;
    private final NewsAssembler newsAssembler;
    private final ScanProperties scanProperties;

    public TwitterNewsSource(TwitterClient twitterClient, NewsAssembler newsAssembler, ScanProperties scanProperties) {
        this.twitterClient = twitterClient;
        this.newsAssembler = newsAssembler;
        this.scanProperties = scanProperties;
    }

    @Override
    public List<News> getNews() {
        List<String> keywords = scanProperties.getTwitterKeywords();
        List<String> users = scanProperties.getTwitterUsers();

        LOG.info("Scanning Twitter... { \"keywords\": {} } { \"users\": {} }", keywords.size(), users.size());

        List<Tweet> tweets = new ArrayList<>();

        for (String keyword : keywords) {
            tweets.addAll(tryGetTweetsByKeyword(keyword));
        }

        for (String user : users) {
            tweets.addAll(tryGetTweetsByUser(user));
        }

        List<News> news = tweets.stream()
                .map(newsAssembler::assembleNews)
                .collect(Collectors.toList());

        LOG.info("Scanning Twitter done { \"tweets\": {} }", news.size());

        return news;
    }

    private List<Tweet> tryGetTweetsByUser(String user) {
        try {
            return twitterClient.getTweetsByUser(user);
        } catch (RuntimeException e) {
            LOG.warn("Failed to get tweets {\"user\": \"{}\", \"reason\": \"{}\"}", user, e.getMessage());
            return List.of();
        }
    }

    private List<Tweet> tryGetTweetsByKeyword(String keyword) {
        try {
            return twitterClient.getTweetsByKeyword(keyword);
        } catch (RuntimeException e) {
            LOG.warn("Failed to get tweets {\"keyword\": \"{}\", \"reason\": \"{}\"}", keyword, e.getMessage());
            return List.of();
        }
    }

}
