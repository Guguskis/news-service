package lt.liutikas.reddit.model;

import org.springframework.context.ApplicationEvent;

import java.net.URL;

public class NewsEvent extends ApplicationEvent {

    private String title;
    private URL url;

    public NewsEvent(Object source) {
        super(source);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

}
