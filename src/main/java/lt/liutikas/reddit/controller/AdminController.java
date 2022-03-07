package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.model.NewsEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SimpMessagingTemplate pushTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public AdminController(SimpMessagingTemplate pushTemplate, ApplicationEventPublisher eventPublisher) {
        this.pushTemplate = pushTemplate;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/news/publish")
    public void sendMessage(String headline) {
        NewsEvent event = new NewsEvent(this);
        event.setHeadline(headline);
        eventPublisher.publishEvent(event);
    }

}
