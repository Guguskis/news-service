package lt.liutikas.reddit.domain.usecase.scannews;

import lt.liutikas.reddit.assembler.ScanAssembler;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.entity.scan.ScanResult;
import lt.liutikas.reddit.domain.port.in.CreateScanResultPort;
import lt.liutikas.reddit.domain.port.out.persistence.CreateNewsPort;
import lt.liutikas.reddit.domain.port.out.persistence.QueryScanResultPort;
import lt.liutikas.reddit.event.NewsPublisher;
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

    private final QueryScanResultPort queryScanResultPort;
    private final CreateScanResultPort createScanResultPort;
    private final CreateNewsPort createNewsPort;
    private final ScanAssembler scanAssembler;

    private final NewsPublisher newsPublisher;
    private final List<NewsSource> newsSources;

    public ScanNewsUseCase(QueryScanResultPort queryScanResultPort, CreateScanResultPort createScanResultPort, NewsPublisher newsPublisher, ScanAssembler scanAssembler, List<NewsSource> newsSources, CreateNewsPort createNewsPort) {
        this.queryScanResultPort = queryScanResultPort;
        this.createScanResultPort = createScanResultPort;
        this.newsPublisher = newsPublisher;
        this.scanAssembler = scanAssembler;
        this.newsSources = newsSources;
        this.createNewsPort = createNewsPort;
    }

    public void scanNews() {
        LOG.info("Scanning news... { \"sources\": {} }", newsSources.size());

        List<News> notScannedNews = getNotScannedNews();
        List<News> news = saveNews(notScannedNews);
        List<ScanResult> scanResults = saveScanResults(news);

        LOG.info("Scanning news done { \"scanResults\": {} }", scanResults.size());

        if (!news.isEmpty())
            newsPublisher.publishNews(news);
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
        return queryScanResultPort.findById(news.getUrl()).stream()
                .findFirst()
                .isEmpty();
    }

    private List<News> saveNews(List<News> notScannedNews) {
        return notScannedNews.stream()
                .sorted(Comparator.comparing(News::getCreated))
                .map(createNewsPort::create)
                .collect(Collectors.toList());
    }

    private List<ScanResult> saveScanResults(List<News> news) {
        return news.stream()
                .map(scanAssembler::assembleScanResult)
                .map(createScanResultPort::create)
                .collect(Collectors.toList());
    }

}
