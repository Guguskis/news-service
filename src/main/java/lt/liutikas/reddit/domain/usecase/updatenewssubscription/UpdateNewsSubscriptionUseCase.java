package lt.liutikas.reddit.domain.usecase.updatenewssubscription;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.domain.entity.core.Channel;
import lt.liutikas.reddit.domain.entity.core.SubscriptionActionType;
import lt.liutikas.reddit.domain.port.in.UpdateNewsSubscriptionPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateNewsSubscriptionUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateNewsSubscriptionUseCase.class);

    private final UpdateNewsSubscriptionPort updateNewsSubscriptionPort;

    public UpdateNewsSubscriptionUseCase(UpdateNewsSubscriptionPort updateNewsSubscriptionPort) {
        this.updateNewsSubscriptionPort = updateNewsSubscriptionPort;
    }

    public void updateNewsSubscription(String sessionId, SubscriptionAction action) {

        SubscriptionActionType actionType = action.getActionType();
        Channel channel = action.getChannel();
        List<String> subChannels = action.getSubChannels();

        switch (actionType) {
            case SUBSCRIBE:
                updateNewsSubscriptionPort.subscribe(sessionId, action);
                break;
            case UNSUBSCRIBE:
                updateNewsSubscriptionPort.unsubscribe(sessionId, action);
                break;
            case SET:
                updateNewsSubscriptionPort.unsubscribe(sessionId, channel);
                updateNewsSubscriptionPort.subscribe(sessionId, action);
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
