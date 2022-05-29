package lt.liutikas.reddit.event;

import lt.liutikas.reddit.ActiveUserRegistry;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.User;
import lt.liutikas.reddit.service.NewsSubscriptionTracker;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsPublisher {

    private final ActiveUserRegistry userRegistry;
    private final NewsSubscriptionTracker newsSubscriptionTracker;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NewsPublisher(ActiveUserRegistry userRegistry, NewsSubscriptionTracker newsSubscriptionTracker, SimpMessagingTemplate simpMessagingTemplate) {
        this.userRegistry = userRegistry;
        this.newsSubscriptionTracker = newsSubscriptionTracker;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void publishNews(String sessionId, News news) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        simpMessagingTemplate.convertAndSendToUser(sessionId, "/topic/news", news, headerAccessor.getMessageHeaders());
    }

    public void publishNews(List<News> news) {
        news.sort(this::compareCreatedDesc);
        for (User user : userRegistry.getActiveUsers()) {
            for (News newsItem : news) {
                if (newsSubscriptionTracker.isSubscribed(user, newsItem)) {
                    publishNews(user.getSessionId(), newsItem);
                }
            }
        }
    }

    private int compareCreatedDesc(News n1, News n2) {
        return n1.getCreated().compareTo(n2.getCreated());
    }

}
