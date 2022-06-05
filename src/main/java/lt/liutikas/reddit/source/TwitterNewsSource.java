package lt.liutikas.reddit.source;

import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.client.TwitterClient;
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

    public TwitterNewsSource(TwitterClient twitterClient, NewsAssembler newsAssembler) {
        this.twitterClient = twitterClient;
        this.newsAssembler = newsAssembler;
    }

    @Override
    public List<News> getNews() {
        List<String> keywords = List.of("xrp", "ripple", "bitcoin");
        LOG.info("Scanning tweets... { \"keywords\": {} }", keywords.size());

        List<Tweet> tweets = new ArrayList<>();

        for (String keyword : keywords) {
            tweets.addAll(tryGetTweets(keyword));
        }

        List<News> news = tweets.stream()
                .map(newsAssembler::assembleNews)
                .collect(Collectors.toList());

        LOG.info("Scanning tweets done { \"news\": {} }", news.size());

        return news;
    }

    private List<Tweet> tryGetTweets(String keyword) {
        try {
            return twitterClient.getTweets(keyword);
        } catch (RuntimeException e) {
            LOG.warn("Failed to get tweets {\"keyword\": \"{}\", \"reason\": \"{}\"}", keyword, e.getMessage());
            return List.of();
        }
    }

}
