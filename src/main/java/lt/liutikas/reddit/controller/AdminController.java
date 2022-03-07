package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.model.NewsEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ApplicationEventPublisher eventPublisher;

    public AdminController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/news/publish")
    public void sendMessage(String headline) throws MalformedURLException {
        NewsEvent event = new NewsEvent(this);
        event.setTitle(headline);
        event.setUrl(new URL("https://www.reddit.com/r/worldnews/comments/7xqzqy/trump_says_he_will_not_be_president_for_next/"));
        eventPublisher.publishEvent(event);
    }

}
