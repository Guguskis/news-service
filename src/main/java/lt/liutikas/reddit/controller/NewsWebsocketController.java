package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.model.NewsSubscription;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsWebsocketController {
    private final NewsService newsService;

    public NewsWebsocketController(NewsService newsService) {
        this.newsService = newsService;
    }

    @MessageMapping("/queue/news")
    public void handleNewsSubscription(NewsSubscription message,
                                       @Header("simpSessionId") String sessionId) {
        newsService.processNewsSubscription(sessionId, message);
    }
}
