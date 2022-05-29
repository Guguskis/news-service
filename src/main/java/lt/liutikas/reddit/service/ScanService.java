package lt.liutikas.reddit.service;

import lt.liutikas.reddit.assembler.EventAssembler;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.assembler.ScanAssembler;
import lt.liutikas.reddit.config.properties.ScanProperties;
import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.ScanResult;
import lt.liutikas.reddit.repository.NewsRepository;
import lt.liutikas.reddit.repository.ScanResultRepository;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import some.developer.reddit.client.RedditClient;
import some.developer.reddit.client.model.PageCategory;
import some.developer.reddit.client.model.Submission;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScanService {

    private static final Logger LOG = LoggerFactory.getLogger(ScanService.class);

    private final RedditClient redditClient;
    private final ScanResultRepository scanResultRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NewsAssembler newsAssembler;
    private final NewsSubscriptionTracker subscriptionTracker;
    private final ScanProperties scanProperties;
    private final ScanAssembler scanAssembler;
    private final NewsRepository newsRepository;

    public ScanService(RedditClient redditClient, ScanResultRepository scanResultRepository, ApplicationEventPublisher eventPublisher, NewsAssembler newsAssembler, NewsSubscriptionTracker subscriptionTracker, ScanProperties scanProperties, ScanAssembler scanAssembler, NewsRepository newsRepository) {
        this.redditClient = redditClient;
        this.scanResultRepository = scanResultRepository;
        this.eventPublisher = eventPublisher;
        this.newsAssembler = newsAssembler;
        this.subscriptionTracker = subscriptionTracker;
        this.scanProperties = scanProperties;
        this.scanAssembler = scanAssembler;
        this.newsRepository = newsRepository;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void scanReddit() {
        LOG.info("Scanning reddit...");

        List<String> subreddits = getSubredditsToScanStream()
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());

        List<Submission> submissions = getNewSubmissions(subreddits);

        List<ScanResult> scanResults = submissions.stream()
                .map(scanAssembler::assembleScanResult)
                .map(scanResultRepository::save)
                .collect(Collectors.toList());

        submissions.stream()
                .sorted(Comparator.comparing(Submission::getCreated))
                .map(newsAssembler::assembleNews)
                .map(newsRepository::save)
                .map(EventAssembler::assembleSavedNewsEvent)
                .forEach(eventPublisher::publishEvent);

        LOG.info("Scanning reddit done. {\"subreddits\": \"{}\", \"submissions\": \"{}\"}", Strings.join(subreddits, ','), scanResults.size());
    }

    private Stream<String> getSubredditsToScanStream() {
        return Stream.concat(
                scanProperties.getSubreddits().stream(),
                subscriptionTracker.getSubChannels(Channel.REDDIT).stream());
    }

    private List<Submission> getNewSubmissions(List<String> subreddits) {
        List<Submission> submissions = subreddits.stream()
                .flatMap(this::tryGetNewSubmissionsStream)
                .collect(Collectors.toList());

        List<URL> urls = submissions.stream()
                .map(Submission::getUrl)
                .collect(Collectors.toList());

        List<URL> scannedUrls = scanResultRepository.findAllById(urls).stream()
                .map(ScanResult::getUrl)
                .collect(Collectors.toList());

        return submissions.stream()
                .filter(submission -> !scannedUrls.contains(submission.getUrl()))
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
