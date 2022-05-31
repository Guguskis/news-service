package lt.liutikas.reddit.event;

import lt.liutikas.reddit.api.model.NewsSubscription;
import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.SubscriptionAction;
import lt.liutikas.reddit.model.event.NewsSubscriptionEvent;
import lt.liutikas.reddit.service.NewsSubscriptionTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsSubscriptionEventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(NewsSubscriptionEventProcessor.class);

    private final NewsSubscriptionTracker newsSubscriptionTracker;

    public NewsSubscriptionEventProcessor(NewsSubscriptionTracker newsSubscriptionTracker) {
        this.newsSubscriptionTracker = newsSubscriptionTracker;
    }

    @EventListener
    public void updateNewsSubscriptionRegistry(NewsSubscriptionEvent event) {
        String sessionId = event.getSessionId();
        NewsSubscription subscription = event.getNewsSubscription();

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

        LOG.info("Subscription event {\"sessionId\": \"{}\", \"channel\": \"{}\", \"subChannels\": {}, \"action\": {} }",
                sessionId,
                channel,
                subChannels,
                action);
    }


}
