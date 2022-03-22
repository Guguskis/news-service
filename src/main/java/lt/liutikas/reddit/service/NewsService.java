package lt.liutikas.reddit.service;

import lt.liutikas.reddit.ActiveUserRegistry;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.NewsPage;
import lt.liutikas.reddit.model.NewsSubscriptionMessage;
import lt.liutikas.reddit.model.User;
import lt.liutikas.reddit.model.event.ScannedNewsEvent;
import lt.liutikas.reddit.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsService.class);

    private final SimpMessagingTemplate pushTemplate;
    private final NewsRepository newsRepository;
    private final NewsAssembler newsAssembler;
    private final NewsSubscriptionTracker newsSubscriptionTracker;
    private final ActiveUserRegistry userRegistry;

    public NewsService(SimpMessagingTemplate pushTemplate, NewsRepository newsRepository, NewsAssembler newsAssembler, NewsSubscriptionTracker newsSubscriptionTracker, ActiveUserRegistry userRegistry) {
        this.pushTemplate = pushTemplate;
        this.newsRepository = newsRepository;
        this.newsAssembler = newsAssembler;
        this.newsSubscriptionTracker = newsSubscriptionTracker;
        this.userRegistry = userRegistry;
    }

    // todo try @SubscribeMapping
    // https://stackoverflow.com/questions/24890450/spring-stomp-subscribemapping-for-user-destination

    @EventListener
    public void handleScannedNewsEvent(ScannedNewsEvent event) {
        News news = newsAssembler.assembleNews(event);
        news = newsRepository.save(news);
        for (User user : userRegistry.getActiveUsers()) {
            if (isSubscribed(user, news)) {
                sendToUser(user, news);
            }
        }
        LOG.info("Published news \"{}\"", news.getTitle());
    }

    private void sendToUser(User user, News news) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(user.getSessionId());
        headerAccessor.setLeaveMutable(true);

        pushTemplate.convertAndSendToUser(user.getSessionId(), "/topic/news", news, headerAccessor.getMessageHeaders());
    }

    private boolean isSubscribed(User user, News news) {
        String sessionId = user.getSessionId();
        List<String> subreddits = newsSubscriptionTracker.getSubreddits(sessionId);
        return subreddits.contains(news.getSubChannel());
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

    public void processNewsSubscription(String sessionId, NewsSubscriptionMessage message) {
        if (message.isSubscribe())
            newsSubscriptionTracker.subscribeSubreddits(sessionId, message.getSubreddits());
        else
            newsSubscriptionTracker.unsubscribeSubreddits(sessionId, message.getSubreddits());

        LOG.info("Subscription event {\"sessionId\": \"{}\", \"subreddits\": {}, \"subscribe\": {}}",
                sessionId,
                message.getSubreddits(),
                message.isSubscribe());
    }
}
