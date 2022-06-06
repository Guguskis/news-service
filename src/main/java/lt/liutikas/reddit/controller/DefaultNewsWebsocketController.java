package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.api.controller.NewsWebsocketController;
import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.event.EventPublisher;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultNewsWebsocketController implements NewsWebsocketController {
    private final EventPublisher eventPublisher;

    public DefaultNewsWebsocketController(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    @MessageMapping("/queue/news")
    public void handleNewsSubscription(@Header("simpSessionId") String sessionId, SubscriptionAction action) {
        eventPublisher.publishNewsSubscriptionEvent(sessionId, action);
    }
}
