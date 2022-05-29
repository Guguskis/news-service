package lt.liutikas.reddit.event;

import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.event.UpdatedNewsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatedNewsEventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatedNewsEventProcessor.class);

    private final NewsPublisher newsPublisher;

    public UpdatedNewsEventProcessor(NewsPublisher newsPublisher) {
        this.newsPublisher = newsPublisher;
    }

    @EventListener
    public void notifySubscribers(UpdatedNewsEvent event) {
        List<News> news = event.getNews();

        newsPublisher.publishNews(news);

        LOG.info("Notified subscribers about updated news { \"count\": {} }", news.size());
    }

}
