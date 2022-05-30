package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.api.controller.NewsWebsocketController;
import lt.liutikas.reddit.api.model.NewsSubscription;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultNewsWebsocketController implements NewsWebsocketController {
    private final NewsService newsService;

    public DefaultNewsWebsocketController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public void handleNewsSubscription(NewsSubscription message, String sessionId) {
        newsService.processNewsSubscription(sessionId, message);
    }
}
