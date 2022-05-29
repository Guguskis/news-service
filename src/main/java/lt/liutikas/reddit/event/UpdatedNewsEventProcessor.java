package lt.liutikas.reddit.event;

import lt.liutikas.reddit.ActiveUserRegistry;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.ProcessingStatus;
import lt.liutikas.reddit.model.SentimentResult;
import lt.liutikas.reddit.model.User;
import lt.liutikas.reddit.model.event.UpdatedNewsEvent;
import lt.liutikas.reddit.repository.SentimentResultRepository;
import lt.liutikas.reddit.service.NewsService;
import lt.liutikas.reddit.service.NewsSubscriptionTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class UpdatedNewsEventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatedNewsEventProcessor.class);

    private final ActiveUserRegistry userRegistry;
    private final NewsSubscriptionTracker newsSubscriptionTracker;
    private final SentimentResultRepository sentimentResultRepository;
    private final SimpMessagingTemplate websocketTemplate;
    private final NewsService newsService;

    public UpdatedNewsEventProcessor(ActiveUserRegistry userRegistry, SentimentResultRepository sentimentResultRepository, ApplicationEventPublisher eventPublisher, NewsSubscriptionTracker newsSubscriptionTracker, SimpMessagingTemplate websocketTemplate, NewsService newsService) {
        this.userRegistry = userRegistry;
        this.sentimentResultRepository = sentimentResultRepository;
        this.newsSubscriptionTracker = newsSubscriptionTracker;
        this.websocketTemplate = websocketTemplate;
        this.newsService = newsService;
    }

    @EventListener
    public void queueSentimentUpdate(UpdatedNewsEvent event) {
        News news = event.getNews();
        SentimentResult sentimentResult = assembleNotStartedSentimentResult(news);
        sentimentResultRepository.save(sentimentResult);
    }

    @EventListener
    public void notifySubscribers(UpdatedNewsEvent event) {
        News news = event.getNews();

        for (User user : userRegistry.getActiveUsers()) {
            publishNews(news, user);
        }
//        eventPublisher.publishEvent(news);

        LOG.info("Published news \"{}\"", news.getTitle());
    }

    private void publishNews(News news, User user) {
        if (newsSubscriptionTracker.isSubscribed(user, news)) {
            newsService.publishNews(user.getSessionId(), news);
        }
    }

    private SentimentResult assembleNotStartedSentimentResult(News news) {
        SentimentResult sentimentResult = new SentimentResult();
        sentimentResult.setNews(news);
        sentimentResult.setStatus(ProcessingStatus.NOT_STARTED);
        return sentimentResult;
    }

}
