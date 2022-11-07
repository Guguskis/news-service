package lt.liutikas.reddit.adapter;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.domain.entity.core.Channel;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.port.in.UpdateNewsSubscriptionPort;
import lt.liutikas.reddit.domain.port.out.cache.QueryNewsSubscriptionPort;
import lt.liutikas.reddit.registry.NewsSubscriptionRegistry;
import org.springframework.stereotype.Service;

@Service
public class NewsSubscriptionCacheAdapter implements UpdateNewsSubscriptionPort, QueryNewsSubscriptionPort {

    private final NewsSubscriptionRegistry newsSubscriptionRegistry;

    public NewsSubscriptionCacheAdapter(NewsSubscriptionRegistry newsSubscriptionRegistry) {
        this.newsSubscriptionRegistry = newsSubscriptionRegistry;
    }

    @Override
    public boolean isSubscribed(String sessionId, News news) {
        return newsSubscriptionRegistry.isSubscribed(sessionId, news);
    }

    @Override
    public void subscribe(String sessionId, SubscriptionAction action) {
        newsSubscriptionRegistry.subscribe(sessionId, action);
    }

    @Override
    public void unsubscribe(String sessionId, SubscriptionAction action) {
        newsSubscriptionRegistry.unsubscribe(sessionId, action);
    }

    @Override
    public void unsubscribe(String sessionId, Channel channel) {
        newsSubscriptionRegistry.unsubscribe(sessionId, channel);
    }

}
