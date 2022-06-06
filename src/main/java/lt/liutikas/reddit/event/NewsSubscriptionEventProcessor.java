package lt.liutikas.reddit.event;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.model.core.Channel;
import lt.liutikas.reddit.model.core.SubscriptionActionType;
import lt.liutikas.reddit.model.event.NewsSubscriptionEvent;
import lt.liutikas.reddit.registry.NewsSubscriptionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsSubscriptionEventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(NewsSubscriptionEventProcessor.class);

    private final NewsSubscriptionRegistry newsSubscriptionRegistry;

    public NewsSubscriptionEventProcessor(NewsSubscriptionRegistry newsSubscriptionRegistry) {
        this.newsSubscriptionRegistry = newsSubscriptionRegistry;
    }

    @EventListener
    public void updateNewsSubscriptionRegistry(NewsSubscriptionEvent event) {
        String sessionId = event.getSessionId();
        SubscriptionAction action = event.getSubscriptionAction();

        SubscriptionActionType actionType = action.getActionType();
        Channel channel = action.getChannel();
        List<String> subChannels = action.getSubChannels();

        switch (actionType) {
            case SUBSCRIBE:
                newsSubscriptionRegistry.subscribe(sessionId, action);
                break;
            case UNSUBSCRIBE:
                newsSubscriptionRegistry.unsubscribe(sessionId, action);
                break;
            case SET:
                newsSubscriptionRegistry.unsubscribe(sessionId, channel);
                newsSubscriptionRegistry.subscribe(sessionId, action);
                break;
            default:
                throw new IllegalArgumentException("Action not implemented: " + actionType);
        }

        LOG.info("Subscription event {\"sessionId\": \"{}\", \"channel\": \"{}\", \"subChannels\": {}, \"actionType\": {} }",
                sessionId,
                channel,
                subChannels,
                actionType);
    }


}
