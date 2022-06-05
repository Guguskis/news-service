package lt.liutikas.reddit.service;

import lt.liutikas.reddit.assembler.ScanAssembler;
import lt.liutikas.reddit.event.EventPublisher;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.scan.ScanResult;
import lt.liutikas.reddit.repository.NewsRepository;
import lt.liutikas.reddit.repository.ScanResultRepository;
import lt.liutikas.reddit.source.NewsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScanService {

    private static final Logger LOG = LoggerFactory.getLogger(ScanService.class);

    private final ScanResultRepository scanResultRepository;
    private final NewsRepository newsRepository;
    private final EventPublisher eventPublisher;
    private final ScanAssembler scanAssembler;
    private final List<NewsSource> newsSources;

    public ScanService(ScanResultRepository scanResultRepository, EventPublisher eventPublisher, ScanAssembler scanAssembler, List<NewsSource> newsSources, NewsRepository newsRepository) {
        this.scanResultRepository = scanResultRepository;
        this.eventPublisher = eventPublisher;
        this.scanAssembler = scanAssembler;
        this.newsSources = newsSources;
        this.newsRepository = newsRepository;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void scan() {
        List<News> notScannedNews = getNewsFromAllSources().stream()
                .filter(this::checkNotScanned)
                .collect(Collectors.toList());

        List<News> news = saveNews(notScannedNews);
        List<ScanResult> scanResults = saveScanResults(news);

        LOG.info("Scanning done. { \"scanResults\": \"{}\" }", scanResults.size());

        if (!news.isEmpty())
            eventPublisher.publishSavedNewsEvent(news);
    }

    private List<News> getNewsFromAllSources() {
        return newsSources.stream()
                .parallel()
                .map(NewsSource::getNews)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private boolean checkNotScanned(News news) {
        return scanResultRepository.findById(news.getUrl()).stream()
                .findFirst()
                .isEmpty();
    }

    private List<News> saveNews(List<News> notScannedNews) {
        return notScannedNews.stream()
                .sorted(Comparator.comparing(News::getCreated))
                .map(newsRepository::save)
                .collect(Collectors.toList());
    }

    private List<ScanResult> saveScanResults(List<News> news) {
        return news.stream()
                .map(scanAssembler::assembleScanResult)
                .map(scanResultRepository::save)
                .collect(Collectors.toList());
    }

}
