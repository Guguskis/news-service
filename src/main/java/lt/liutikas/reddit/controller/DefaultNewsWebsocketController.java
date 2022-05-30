package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.api.controller.NewsWebsocketController;
import lt.liutikas.reddit.api.model.NewsSubscription;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultNewsWebsocketController implements NewsWebsocketController {
    private final NewsService newsService;

    public DefaultNewsWebsocketController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    @MessageMapping("/queue/news")
    public void handleNewsSubscription(NewsSubscription message, @Header("simpSessionId") String sessionId) {
        newsService.processNewsSubscription(sessionId, message);
    }
}
