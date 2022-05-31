package lt.liutikas.reddit.source;

import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.config.properties.ScanProperties;
import lt.liutikas.reddit.model.core.Channel;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.registry.NewsSubscriptionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import some.developer.reddit.client.RedditClient;
import some.developer.reddit.client.model.PageCategory;
import some.developer.reddit.client.model.Submission;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RedditNewsSource implements NewsSource {

    private static final Logger LOG = LoggerFactory.getLogger(RedditNewsSource.class);

    private final RedditClient redditClient;
    private final NewsAssembler newsAssembler;
    private final NewsSubscriptionRegistry subscriptionTracker;
    private final ScanProperties scanProperties;

    public RedditNewsSource(RedditClient redditClient, NewsAssembler newsAssembler, NewsSubscriptionRegistry subscriptionTracker, ScanProperties scanProperties) {
        this.redditClient = redditClient;
        this.newsAssembler = newsAssembler;
        this.subscriptionTracker = subscriptionTracker;
        this.scanProperties = scanProperties;
    }

    @Override
    public List<News> getNews() {
        List<String> subreddits = getSubredditsToScan();
        LOG.info("Scanning subreddits { \"count\": \"{}\" }", subreddits.size());

        return subreddits.stream()
                .flatMap(this::tryGetNewSubmissionsStream)
                .map(newsAssembler::assembleNews)
                .collect(Collectors.toList());
    }


    private List<String> getSubredditsToScan() {
        return Stream.concat(
                        scanProperties.getSubreddits().stream(),
                        subscriptionTracker.getSubChannels(Channel.REDDIT).stream())
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
    }


    private Stream<? extends Submission> tryGetNewSubmissionsStream(String subreddit) {
        try {
            return redditClient.getSubmissions(subreddit, PageCategory.NEW).stream();
        } catch (HttpClientErrorException e) {
            LOG.warn("Failed to get submissions {\"subreddit\": \"{}\", \"reason\": \"{}\"}", subreddit, e.getStatusText());
            return Stream.empty();
        } catch (ResourceAccessException e) {
            LOG.warn("Failed to get submissions {\"subreddit\": \"{}\", \"reason\": \"{}\"}", subreddit, e.getMessage());
            return Stream.empty();
        }
    }

}
