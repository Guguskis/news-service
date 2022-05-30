package lt.liutikas.reddit.api.controller;

import lt.liutikas.reddit.api.model.NewsSubscription;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;

public interface NewsWebsocketController {
    @MessageMapping("/queue/news")
    void handleNewsSubscription(NewsSubscription message, @Header("simpSessionId") String sessionId);
}
