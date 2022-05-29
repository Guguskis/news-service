package lt.liutikas.reddit.event;

import lt.liutikas.reddit.ActiveUserRegistry;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.ProcessingStatus;
import lt.liutikas.reddit.model.SentimentResult;
import lt.liutikas.reddit.model.User;
import lt.liutikas.reddit.model.event.UpdatedNewsEvent;
import lt.liutikas.reddit.repository.SentimentResultRepository;
import lt.liutikas.reddit.service.NewsSubscriptionTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatedNewsEventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatedNewsEventProcessor.class);

    private final ActiveUserRegistry userRegistry;
    private final ApplicationEventPublisher eventPublisher;
    private final NewsSubscriptionTracker newsSubscriptionTracker;
    private final SentimentResultRepository sentimentResultRepository;
    private final SimpMessagingTemplate websocketTemplate;

    public UpdatedNewsEventProcessor(ActiveUserRegistry userRegistry, SentimentResultRepository sentimentResultRepository, ApplicationEventPublisher eventPublisher, NewsSubscriptionTracker newsSubscriptionTracker, SimpMessagingTemplate websocketTemplate) {
        this.userRegistry = userRegistry;
        this.sentimentResultRepository = sentimentResultRepository;
        this.eventPublisher = eventPublisher;
        this.newsSubscriptionTracker = newsSubscriptionTracker;
        this.websocketTemplate = websocketTemplate;
    }

    @EventListener
    public void queueSentimentUpdate(UpdatedNewsEvent event) {
        News news = event.getNews();
        SentimentResult sentimentResult = new SentimentResult();
        sentimentResult.setNews(news);
        sentimentResult.setStatus(ProcessingStatus.NOT_STARTED);

        sentimentResultRepository.save(sentimentResult);
    }

    @EventListener
    public void notifySubscribers(UpdatedNewsEvent event) {
        News news = event.getNews();

        for (User user : userRegistry.getActiveUsers()) {
            if (isSubscribed(user, news)) {
                sendNews(user.getSessionId(), news);
            }
        }

//        eventPublisher.publishEvent(news);

        LOG.info("Published news \"{}\"", news.getTitle());
    }

    private boolean isSubscribed(User user, News news) { // todo move to subscriptionTracker
        List<String> subChannels = newsSubscriptionTracker.getSubChannels(user.getSessionId(), news.getChannel());
        return subChannels.contains(news.getSubChannel());
    }

    private void sendNews(String sessionId, News news) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        websocketTemplate.convertAndSendToUser(sessionId, "/topic/news", news, headerAccessor.getMessageHeaders());
    }

}
