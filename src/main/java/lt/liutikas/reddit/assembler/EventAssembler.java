package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.event.SavedNewsEvent;

public class EventAssembler {

    public static SavedNewsEvent assembleSavedNewsEvent(News news) {
        SavedNewsEvent event = new SavedNewsEvent(news);

        event.setNews(news);

        return event;
    }
}
