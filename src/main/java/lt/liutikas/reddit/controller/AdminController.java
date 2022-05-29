package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.ActiveUserRegistry;
import lt.liutikas.reddit.event.EventPublisher;
import lt.liutikas.reddit.model.User;
import lt.liutikas.reddit.service.ScanService;
import lt.liutikas.reddit.service.SentimentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final EventPublisher eventPublisher;
    private final ActiveUserRegistry activeUserRegistry;
    private final ScanService scanService;
    private final SentimentService sentimentService;

    public AdminController(EventPublisher eventPublisher, ActiveUserRegistry activeUserRegistry, ScanService scanService, SentimentService sentimentService) {
        this.eventPublisher = eventPublisher;
        this.activeUserRegistry = activeUserRegistry;
        this.scanService = scanService;
        this.sentimentService = sentimentService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return activeUserRegistry.getActiveUsers();
    }

    @PostMapping("/news/reddit/scan")
    public void scanNews() {
        scanService.scanReddit();
    }

    @PostMapping("/news/sentiments/process")
    public void processSentiment() {
        sentimentService.processSentiments();
    }

}
