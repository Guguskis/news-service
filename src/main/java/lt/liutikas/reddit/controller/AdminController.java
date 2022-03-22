package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.ActiveUserRegistry;
import lt.liutikas.reddit.model.User;
import lt.liutikas.reddit.model.event.ScannedNewsEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public AdminController(ApplicationEventPublisher eventPublisher, ActiveUserRegistry activeUserRegistry) {
        this.eventPublisher = eventPublisher;
        this.activeUserRegistry = activeUserRegistry;
    }

    @PostMapping("/news/publish")
    public void sendMessage(String headline) throws MalformedURLException {
        ScannedNewsEvent event = new ScannedNewsEvent(this);
        event.setTitle(headline);
        event.setUrl(new URL("https://www.reddit.com/r/worldnews/comments/7xqzqy/trump_says_he_will_not_be_president_for_next/"));
        event.setCreated(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        eventPublisher.publishEvent(event);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return activeUserRegistry.getActiveUsers();
    }

}
