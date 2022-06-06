package lt.liutikas.reddit.event;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.assembler.EventAssembler;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.event.NewsSubscriptionEvent;
import lt.liutikas.reddit.model.event.SavedNewsEvent;
import lt.liutikas.reddit.model.event.UpdatedNewsEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventPublisher {

    private final ApplicationEventPublisher publisher;
    private final EventAssembler eventAssembler;

    public EventPublisher(ApplicationEventPublisher publisher, EventAssembler eventAssembler) {
        this.publisher = publisher;
        this.eventAssembler = eventAssembler;
    }

    public void publishUpdatedNewsEvent(List<News> news) {
        UpdatedNewsEvent event = eventAssembler.assembleUpdatedNewsEvent(news);
        publisher.publishEvent(event);
    }

    public void publishSavedNewsEvent(List<News> news) {
        SavedNewsEvent event = eventAssembler.assembleSavedNewsEvent(news);
        publisher.publishEvent(event);
    }

    public void publishNewsSubscriptionEvent(String sessionId, SubscriptionAction action) {
        NewsSubscriptionEvent event = eventAssembler.assembleNewsSubscriptionEvent(sessionId, action);
        publisher.publishEvent(event);
    }
}
