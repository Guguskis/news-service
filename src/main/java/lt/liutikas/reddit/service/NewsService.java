package lt.liutikas.reddit.service;

import lt.liutikas.reddit.model.NewsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsService.class);

    @EventListener
    public void handleEvent(NewsEvent event) {
        LOG.info("Published event \"{}\"", event.getHeadline());
    }

}
