package lt.liutikas.reddit.domain.port.in.cache;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.domain.entity.core.Channel;

public interface UpdateNewsSubscriptionPort {

    void subscribe(String sessionId, SubscriptionAction action);

    void unsubscribe(String sessionId, SubscriptionAction action);

    void unsubscribe(String sessionId, Channel channel);

}
