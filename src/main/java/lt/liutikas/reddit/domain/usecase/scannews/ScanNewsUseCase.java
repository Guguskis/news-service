package lt.liutikas.reddit.domain.usecase.scannews;

import lt.liutikas.reddit.assembler.ScanAssembler;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.entity.scan.ScanResult;
import lt.liutikas.reddit.event.EventPublisher;
import lt.liutikas.reddit.repository.NewsRepository;
import lt.liutikas.reddit.repository.ScanResultRepository;
import lt.liutikas.reddit.source.NewsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScanNewsUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ScanNewsUseCase.class);

    private final ScanResultRepository scanResultRepository;
    private final NewsRepository newsRepository;
    private final EventPublisher eventPublisher;
    private final ScanAssembler scanAssembler;
    private final List<NewsSource> newsSources;

    public ScanNewsUseCase(ScanResultRepository scanResultRepository, NewsRepository newsRepository, EventPublisher eventPublisher, ScanAssembler scanAssembler, List<NewsSource> newsSources) {
        this.scanResultRepository = scanResultRepository;
        this.newsRepository = newsRepository;
        this.eventPublisher = eventPublisher;
        this.scanAssembler = scanAssembler;
        this.newsSources = newsSources;
    }

    public void scanNews() {
        LOG.info("Scanning news... { \"sources\": {} }", newsSources.size());

        List<News> notScannedNews = getNotScannedNews();
        List<News> news = saveNews(notScannedNews);
        List<ScanResult> scanResults = saveScanResults(news);

        LOG.info("Scanning news done { \"scanResults\": {} }", scanResults.size());

        if (!news.isEmpty())
            eventPublisher.publishSavedNewsEvent(news);
    }

    private List<News> getNotScannedNews() {
        return getNews(newsSources).stream()
                .filter(this::notScanned)
                .collect(Collectors.toList());
    }

    private List<News> getNews(List<NewsSource> sources) {
        return sources.stream()
                .parallel()
                .map(NewsSource::getNews)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private boolean notScanned(News news) {
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
