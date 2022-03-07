package lt.liutikas.reddit.service;

import lt.liutikas.reddit.model.NewsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NewsPushService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsPushService.class);

    private final SimpMessagingTemplate pushTemplate;

    public NewsPushService(SimpMessagingTemplate pushTemplate) {
        this.pushTemplate = pushTemplate;
    }

    @EventListener
    public void handleEvent(NewsEvent event) {
        pushTemplate.convertAndSend("/topic/news", event);
        LOG.info("Published event \"{}\"", event.getHeadline());
    }

}
