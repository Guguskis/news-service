package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.event.UpdatedNewsEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventAssembler {

    public UpdatedNewsEvent assembleSavedNewsEvent(List<News> news) {
        UpdatedNewsEvent event = new UpdatedNewsEvent(news);

        event.setNews(news);

        return event;
    }
}
