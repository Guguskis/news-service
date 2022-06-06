package lt.liutikas.reddit.api.controller;

import lt.liutikas.reddit.api.model.SubscriptionAction;

public interface NewsWebsocketController {
    void handleNewsSubscription(String sessionId, SubscriptionAction message);
}
