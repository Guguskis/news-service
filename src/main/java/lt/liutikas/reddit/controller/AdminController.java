package lt.liutikas.reddit.controller;

import com.azure.ai.textanalytics.models.DocumentSentiment;
import lt.liutikas.reddit.ActiveUserRegistry;
import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.User;
import lt.liutikas.reddit.model.event.SavedNewsEvent;
import lt.liutikas.reddit.repository.NewsRepository;
import lt.liutikas.reddit.service.ScanService;
import lt.liutikas.reddit.service.SentimentService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ApplicationEventPublisher eventPublisher;
    private final ActiveUserRegistry activeUserRegistry;
    private final ScanService scanService;
    private final SentimentService sentimentService;
    private final NewsRepository newsRepository;

    public AdminController(ApplicationEventPublisher eventPublisher, ActiveUserRegistry activeUserRegistry, ScanService scanService, SentimentService sentimentService, NewsRepository newsRepository) {
        this.eventPublisher = eventPublisher;
        this.activeUserRegistry = activeUserRegistry;
        this.scanService = scanService;
        this.sentimentService = sentimentService;
        this.newsRepository = newsRepository;
    }

    @PostMapping("/news/publish")
    public void sendMessage(String headline, Channel channel, String subChannel) throws MalformedURLException {
        News news = new News();
        news.setTitle(headline);
        news.setUrl(new URL("https://www.reddit.com/r/worldnews/comments/7xqzqy/trump_says_he_will_not_be_president_for_next/"));
        news.setCreated(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        news.setChannel(channel);
        news.setSubChannel(subChannel);

        SavedNewsEvent event = new SavedNewsEvent(this);
        event.setNews(newsRepository.save(news));
        eventPublisher.publishEvent(event);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return activeUserRegistry.getActiveUsers();
    }

    @PostMapping("/news/reddit/scan")
    public void scanNews() {
        scanService.scanReddit();
    }

    @PostMapping("/admin/sentiment")
    public DocumentSentiment getSentiment(@RequestBody News news) {
        return sentimentService.getSentiment(news.getTitle());
    }

}
