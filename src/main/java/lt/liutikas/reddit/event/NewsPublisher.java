package lt.liutikas.reddit.event;

import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.core.User;
import lt.liutikas.reddit.registry.ActiveUserRegistry;
import lt.liutikas.reddit.registry.NewsSubscriptionRegistry;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsPublisher {

    private final ActiveUserRegistry userRegistry;
    private final NewsSubscriptionRegistry newsSubscriptionRegistry;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NewsPublisher(ActiveUserRegistry userRegistry, NewsSubscriptionRegistry newsSubscriptionRegistry, SimpMessagingTemplate simpMessagingTemplate) {
        this.userRegistry = userRegistry;
        this.newsSubscriptionRegistry = newsSubscriptionRegistry;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void publishNews(String sessionId, News news) {
        MessageHeaders headers = getMessageHeaders(sessionId);
        simpMessagingTemplate.convertAndSendToUser(sessionId, "/topic/news", news, headers);
    }

    public void publishNews(List<News> news) {
        news.sort(this::compareCreatedDesc);
        for (User user : userRegistry.getActiveUsers()) {
            for (News newsItem : news) {
                if (newsSubscriptionRegistry.isSubscribed(user, newsItem)) {
                    publishNews(user.getSessionId(), newsItem);
                }
            }
        }
    }

    private MessageHeaders getMessageHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    private int compareCreatedDesc(News n1, News n2) {
        return n1.getCreated().compareTo(n2.getCreated());
    }

}
