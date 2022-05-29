package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.event.UpdatedNewsEvent;

public class EventAssembler {

    public static UpdatedNewsEvent assembleSavedNewsEvent(News news) {
        UpdatedNewsEvent event = new UpdatedNewsEvent(news);

        event.setNews(news);

        return event;
    }
}
