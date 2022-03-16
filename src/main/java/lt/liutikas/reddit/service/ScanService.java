package lt.liutikas.reddit.service;

import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.ScanResult;
import lt.liutikas.reddit.repository.ScanResultRepository;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import some.developer.reddit.client.RedditClient;
import some.developer.reddit.client.model.PageCategory;
import some.developer.reddit.client.model.Submission;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScanService {

    private static final Logger LOG = LoggerFactory.getLogger(ScanService.class);
    private static final List<String> SUBREDDITS = Arrays.asList("ukraine", "combatFootage");

    private final RedditClient redditClient;
    private final ScanResultRepository scanResultRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NewsAssembler newsAssembler;
    private final NewsSubscriptionTracker subscriptionTracker;

    public ScanService(RedditClient redditClient, ScanResultRepository scanResultRepository, ApplicationEventPublisher eventPublisher, NewsAssembler newsAssembler, NewsSubscriptionTracker subscriptionTracker) {
        this.redditClient = redditClient;
        this.scanResultRepository = scanResultRepository;
        this.eventPublisher = eventPublisher;
        this.newsAssembler = newsAssembler;
        this.subscriptionTracker = subscriptionTracker;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void scanReddit() {
        LOG.info("Scanning reddit...");

        List<String> subreddits = Stream.concat(SUBREDDITS.stream(), subscriptionTracker.getSubreddits().stream())
                .distinct()
                .collect(Collectors.toList());

        List<Submission> submissions = subreddits.stream()
                .flatMap(this::getNewSubmissionsStream)
                .collect(Collectors.toList());

        List<URL> urls = submissions.stream()
                .map(Submission::getUrl)
                .collect(Collectors.toList());
        List<URL> scannedUrls = scanResultRepository.findAllById(urls).stream()
                .map(ScanResult::getUrl)
                .collect(Collectors.toList());

        List<Submission> notScannedSubmissions = submissions.stream()
                .filter(submission -> !scannedUrls.contains(submission.getUrl()))
                .collect(Collectors.toList());

        List<ScanResult> scanResults = notScannedSubmissions.stream()
                .map(ScanService::assembleScanResult)
                .collect(Collectors.toList());

        scanResultRepository.saveAll(scanResults);

        LOG.info("Scanning reddit done. {\"subreddits\": \"{}\", \"submissions\": \"{}\"}", Strings.join(subreddits, ','), scanResults.size());

        notScannedSubmissions.stream()
                .map(newsAssembler::assembleScannedNewsEvent)
                .forEach(eventPublisher::publishEvent);
    }

    private Stream<? extends Submission> getNewSubmissionsStream(String subreddit) {
        return redditClient.getSubmissions(subreddit, PageCategory.NEW).stream();
    }

    private static ScanResult assembleScanResult(Submission submission) {
        ScanResult scanResult = new ScanResult();

        scanResult.setUrl(submission.getUrl());
        scanResult.setScannedAt(LocalDateTime.now());
        scanResult.setSource(Channel.REDDIT);

        return scanResult;
    }
}
