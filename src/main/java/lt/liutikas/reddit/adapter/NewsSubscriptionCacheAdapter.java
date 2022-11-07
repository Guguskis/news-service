package lt.liutikas.reddit.adapter;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.domain.entity.core.Channel;
import lt.liutikas.reddit.domain.port.in.UpdateNewsSubscriptionPort;
import lt.liutikas.reddit.registry.NewsSubscriptionRegistry;
import org.springframework.stereotype.Service;

@Service
public class NewsSubscriptionCacheAdapter implements UpdateNewsSubscriptionPort {

    private final NewsSubscriptionRegistry newsSubscriptionRegistry;

    public NewsSubscriptionCacheAdapter(NewsSubscriptionRegistry newsSubscriptionRegistry) {
        this.newsSubscriptionRegistry = newsSubscriptionRegistry;
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
