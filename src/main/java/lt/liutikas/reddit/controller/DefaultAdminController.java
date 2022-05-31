package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.api.controller.AdminController;
import lt.liutikas.reddit.model.core.User;
import lt.liutikas.reddit.registry.ActiveUserRegistry;
import lt.liutikas.reddit.service.ScanService;
import lt.liutikas.reddit.service.SentimentService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DefaultAdminController implements AdminController {

    private final ActiveUserRegistry activeUserRegistry;
    private final ScanService scanService;
    private final SentimentService sentimentService;

    public DefaultAdminController(ActiveUserRegistry activeUserRegistry, ScanService scanService, SentimentService sentimentService) {
        this.activeUserRegistry = activeUserRegistry;
        this.scanService = scanService;
        this.sentimentService = sentimentService;
    }

    @Override
    public List<User> getUsers() {
        return activeUserRegistry.getActiveUsers();
    }

    @Override
    public void scanNews() {
        scanService.scan();
    }

    @Override
    public void processSentiment() {
        sentimentService.processSentiments();
    }

}
