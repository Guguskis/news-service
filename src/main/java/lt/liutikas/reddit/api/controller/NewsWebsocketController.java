package lt.liutikas.reddit.api.controller;

import lt.liutikas.reddit.api.model.NewsSubscription;

public interface NewsWebsocketController {
    void handleNewsSubscription(NewsSubscription message, String sessionId);
}
