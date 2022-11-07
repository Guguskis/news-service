package lt.liutikas.reddit.event;

import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.entity.event.SavedNewsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SavedNewsEventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SavedNewsEventProcessor.class);

    private final NewsPublisher newsPublisher;

    public SavedNewsEventProcessor(NewsPublisher newsPublisher) {
        this.newsPublisher = newsPublisher;
    }

    @EventListener
    public void notifySubscribers(SavedNewsEvent event) {
        List<News> news = event.getNews();

        newsPublisher.publishNews(news);

        LOG.info("Notified subscribers about saved news { \"count\": {} }", news.size());
    }

}
