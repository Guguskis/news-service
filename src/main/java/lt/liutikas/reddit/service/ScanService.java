package lt.liutikas.reddit.service;

import lt.liutikas.reddit.client.RedditClient;
import lt.liutikas.reddit.model.ScanResult;
import lt.liutikas.reddit.model.ScanSource;
import lt.liutikas.reddit.model.event.ScannedNewsEvent;
import lt.liutikas.reddit.model.reddit.PageCategory;
import lt.liutikas.reddit.model.reddit.Submission;
import lt.liutikas.reddit.repository.ScanResultRepository;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    public ScanService(RedditClient redditClient, ScanResultRepository scanResultRepository, ApplicationEventPublisher eventPublisher) {
        this.redditClient = redditClient;
        this.scanResultRepository = scanResultRepository;
        this.eventPublisher = eventPublisher;
    }

    private static ScanResult assembleScanResult(Submission submission) {
        ScanResult scanResult = new ScanResult();
        scanResult.setUrl(submission.getUrl());
        scanResult.setScannedAt(LocalDateTime.now());
        scanResult.setSource(ScanSource.REDDIT);
        return scanResult;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void scanReddit() {
        LOG.info("Scanning reddit...");

        List<Submission> submissions = SUBREDDITS.stream()
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

        LOG.info("Scanning reddit done. {\"subreddits\": \"{}\", \"submissions\": \"{}\"}", Strings.join(SUBREDDITS, ','), scanResults.size());

        notScannedSubmissions.stream()
                .map(this::assembleNewsEvent)
                .forEach(eventPublisher::publishEvent);
    }

    private ScannedNewsEvent assembleNewsEvent(Submission submission) {
        ScannedNewsEvent scannedNewsEvent = new ScannedNewsEvent(this);
        scannedNewsEvent.setUrl(submission.getUrl());
        scannedNewsEvent.setTitle(submission.getTitle());
        scannedNewsEvent.setCreated(submission.getCreated());
        return scannedNewsEvent;
    }

    private Stream<? extends Submission> getNewSubmissionsStream(String subreddit) {
        return redditClient.getSubmissions(subreddit, PageCategory.NEW).stream();
    }
}
