package lt.liutikas.reddit.event;

import lt.liutikas.reddit.assembler.EventAssembler;
import lt.liutikas.reddit.model.News;
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
        UpdatedNewsEvent event = eventAssembler.assembleSavedNewsEvent(news);
        publisher.publishEvent(event);
    }

}
