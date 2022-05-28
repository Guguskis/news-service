package lt.liutikas.reddit.model.event;

import lt.liutikas.reddit.model.News;
import org.springframework.context.ApplicationEvent;

public class SavedNewsEvent extends ApplicationEvent {

    private News news;

    public SavedNewsEvent(Object source) {
        super(source);
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
