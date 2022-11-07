package lt.liutikas.reddit.domain.usecase.scannews;

import lt.liutikas.reddit.assembler.ScanAssembler;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.domain.entity.scan.ScanResult;
import lt.liutikas.reddit.domain.port.in.CreateNewsPort;
import lt.liutikas.reddit.domain.port.in.CreateScanResultPort;
import lt.liutikas.reddit.domain.port.out.cache.QueryNewsSubscriptionPort;
import lt.liutikas.reddit.domain.port.out.persistence.QueryActiveUsersPort;
import lt.liutikas.reddit.domain.port.out.persistence.QueryScanResultPort;
import lt.liutikas.reddit.domain.port.out.web.PublishNewsPort;
import lt.liutikas.reddit.source.NewsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScanNewsUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ScanNewsUseCase.class);

    private final QueryScanResultPort queryScanResultPort;
    private final CreateScanResultPort createScanResultPort;
    private final CreateNewsPort createNewsPort;
    private final QueryActiveUsersPort queryActiveUsersPort;
    private final PublishNewsPort publishNewsPort;
    private final ScanAssembler scanAssembler;

    private final List<NewsSource> newsSources;
    private final QueryNewsSubscriptionPort queryNewsSubscriptionPort;

    public ScanNewsUseCase(QueryScanResultPort queryScanResultPort, CreateScanResultPort createScanResultPort, QueryActiveUsersPort queryActiveUsersPort, ScanAssembler scanAssembler, List<NewsSource> newsSources, CreateNewsPort createNewsPort, PublishNewsPort publishNewsPort, QueryNewsSubscriptionPort queryNewsSubscriptionPort) {
        this.queryScanResultPort = queryScanResultPort;
        this.createScanResultPort = createScanResultPort;
        this.queryActiveUsersPort = queryActiveUsersPort;
        this.scanAssembler = scanAssembler;
        this.newsSources = newsSources;
        this.createNewsPort = createNewsPort;
        this.publishNewsPort = publishNewsPort;
        this.queryNewsSubscriptionPort = queryNewsSubscriptionPort;
    }

    @Transactional
    public void scanNews() {
        LOG.info("Scanning news... { \"sources\": {} }", newsSources.size());

        List<News> notScannedNews = getNotScannedNews();
        List<News> news = saveNews(notScannedNews);
        saveScanResults(news);

        LOG.info("Scanning news done { \"news\": {} }", news.size());

        if (news.isEmpty())
            return;

        List<User> activeUsers = queryActiveUsersPort.listActiveUsers();

        LOG.info("Publishing news { \"activeUsers\": {} }", activeUsers.size());

        news.sort(this::compareCreatedDesc);
        for (User user : activeUsers) {
            for (News newsItem : news) {
                if (queryNewsSubscriptionPort.isSubscribed(user.getSessionId(), newsItem)) {
                    publishNewsPort.publishNews(user.getSessionId(), newsItem);
                }
            }
        }

        LOG.info("Published news");
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

    private int compareCreatedDesc(News n1, News n2) {
        return n1.getCreated().compareTo(n2.getCreated());
    }

}
