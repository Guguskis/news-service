package lt.liutikas.reddit.service;

import lt.liutikas.reddit.ActiveUserRegistry;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.model.*;
import lt.liutikas.reddit.model.api.GetNewsRequest;
import lt.liutikas.reddit.model.api.NewsPage;
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
                sendNews(user.getSessionId(), news);
            }
        }

        LOG.info("Published news \"{}\"", news.getTitle());
    }

    public NewsPage getNews(GetNewsRequest request) {
        PageRequest pageRequest = getPageRequestByCreatedDesc(request);
        List<String> subChannels = request.getSubChannels();

        Page<News> page = findNews(pageRequest, subChannels);

        NewsPage newsPage = new NewsPage();
        newsPage.setNews(page.getContent());
        newsPage.setNextToken(page);

        LOG.info("Returning news {'pageToken': {}, 'pageSize': {}}", pageRequest.getPageNumber(), request.getPageSize());

        return newsPage;
    }

    public NewsPage getNews(Channel channel, GetNewsRequest request) {
        PageRequest pageRequest = getPageRequestByCreatedDesc(request);
        List<String> subChannels = request.getSubChannels();
        Page<News> page = findNews(channel, subChannels, pageRequest);

        NewsPage newsPage = new NewsPage();
        newsPage.setNews(page.getContent());
        newsPage.setNextToken(page);

        LOG.info("Returning news {'channel': '{}''pageToken': {}, 'pageSize': {}}", channel, pageRequest.getPageNumber(), request.getPageSize());

        return newsPage;
    }

    public void processNewsSubscription(String sessionId, NewsSubscription subscription) {

        SubscriptionAction action = subscription.getAction();
        Channel channel = subscription.getChannel();
        List<String> subChannels = subscription.getSubChannels();

        switch (action) {
            case SUBSCRIBE:
                newsSubscriptionTracker.subscribe(sessionId, subscription);
                break;
            case UNSUBSCRIBE:
                newsSubscriptionTracker.unsubscribe(sessionId, subscription);
                break;
            case SET:
                newsSubscriptionTracker.unsubscribe(sessionId, channel);
                newsSubscriptionTracker.subscribe(sessionId, subscription);
                break;
            default:
                throw new IllegalArgumentException("Action not implemented: " + action);
        }

        LOG.info("Subscription event {\"sessionId\": \"{}\", \"channel\": \"{}\", \"subChannels\": {}, \"action\": {}}",
                sessionId,
                channel,
                subChannels,
                action);
    }

    private Page<News> findNews(PageRequest pageRequest, List<String> subChannels) {
        if (subChannels.isEmpty()) {
            return newsRepository.findAll(pageRequest);
        } else {
            return newsRepository.findBySubChannelInIgnoreCase(subChannels, pageRequest);
        }
    }

    private Page<News> findNews(Channel channel, List<String> subChannels, PageRequest pageRequest) {
        if (subChannels.isEmpty()) {
            return newsRepository.findByChannel(channel, pageRequest);
        } else {
            return newsRepository.findByChannelAndSubChannelInIgnoreCase(channel, subChannels, pageRequest);
        }
    }

    private boolean isSubscribed(User user, News news) {
        List<String> subChannels = newsSubscriptionTracker.getSubChannels(user.getSessionId(), news.getChannel());
        return subChannels.contains(news.getSubChannel());
    }

    private PageRequest getPageRequestByCreatedDesc(GetNewsRequest request) {
        PageRequest pageRequest = request.pageRequest();
        Sort sort = Sort.by("created").descending();
        return pageRequest.withSort(sort);
    }

    private void sendNews(String sessionId, News news) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        pushTemplate.convertAndSendToUser(sessionId, "/topic/news", news, headerAccessor.getMessageHeaders());
    }
}
