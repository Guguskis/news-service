package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.event.NewsSubscriptionEvent;
import lt.liutikas.reddit.model.event.SavedNewsEvent;
import lt.liutikas.reddit.model.event.UpdatedNewsEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventAssembler {

    public UpdatedNewsEvent assembleUpdatedNewsEvent(List<News> news) {
        UpdatedNewsEvent event = new UpdatedNewsEvent(news);

        event.setNews(news);

        return event;
    }

    public SavedNewsEvent assembleSavedNewsEvent(List<News> news) {
        SavedNewsEvent event = new SavedNewsEvent(news);

        event.setNews(news);

        return event;
    }

    public NewsSubscriptionEvent assembleNewsSubscriptionEvent(SubscriptionAction action, String sessionId) {
        NewsSubscriptionEvent event = new NewsSubscriptionEvent();

        event.setSubscriptionAction(action);
        event.setSessionId(sessionId);

        return event;
    }
}
