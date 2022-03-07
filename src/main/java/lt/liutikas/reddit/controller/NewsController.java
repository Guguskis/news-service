package lt.liutikas.reddit.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final SimpMessagingTemplate pushTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public NewsController(SimpMessagingTemplate pushTemplate, ApplicationEventPublisher eventPublisher) {
        this.pushTemplate = pushTemplate;
        this.eventPublisher = eventPublisher;
    }

    @MessageMapping("/news")
    @SendTo("/topic/news")
    public String news(String message) {

        System.out.println(message);
        return "Hello, " + message + "!";
    }

}
