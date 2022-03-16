package lt.liutikas.reddit.service;

import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.NewsPage;
import lt.liutikas.reddit.model.NewsSubscriptionMessage;
import lt.liutikas.reddit.model.event.ScannedNewsEvent;
import lt.liutikas.reddit.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsService.class);

    private final SimpMessagingTemplate pushTemplate;
    private final NewsRepository newsRepository;
    private final NewsAssembler newsAssembler;
    private final NewsSubscriptionTracker newsSubscriptionTracker;

    public NewsService(SimpMessagingTemplate pushTemplate, NewsRepository newsRepository, NewsAssembler newsAssembler, NewsSubscriptionTracker newsSubscriptionTracker) {
        this.pushTemplate = pushTemplate;
        this.newsRepository = newsRepository;
        this.newsAssembler = newsAssembler;
        this.newsSubscriptionTracker = newsSubscriptionTracker;
    }

    // todo try @SubscribeMapping
    // https://stackoverflow.com/questions/24890450/spring-stomp-subscribemapping-for-user-destination

    @EventListener
    public void handleScannedNewsEvent(ScannedNewsEvent event) {
        News news = newsAssembler.assembleNews(event);
        news = newsRepository.save(news);
        pushTemplate.convertAndSend("/topic/news", news);
        LOG.info("Published news \"{}\"", news.getTitle());
    }

    public NewsPage getAll(PageRequest pageRequest) {

        pageRequest = pageRequest.withSort(Sort.by("created").descending());
        Page<News> page = newsRepository.findAll(pageRequest);

        NewsPage newsPage = new NewsPage();
        newsPage.setNews(page.getContent());
        newsPage.setNextToken(page);

        LOG.info("Returning news {'pageToken': {}, 'pageSize': {}}", pageRequest.getPageNumber(), pageRequest.getPageSize());

        return newsPage;
    }

    public void subscribe(String sessionId, NewsSubscriptionMessage message) {
        newsSubscriptionTracker.subscribeSubreddits(sessionId, message.getSubreddits());
        LOG.info("Subscription event: " + String.join(", ", message.getSubreddits()) + " subscribe:" + message.isSubscribe());
    }
}
