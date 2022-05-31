package lt.liutikas.reddit.model.event;

import lt.liutikas.reddit.model.core.News;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class SavedNewsEvent extends ApplicationEvent {

    private List<News> news;

    public SavedNewsEvent(Object source) {
        super(source);
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
