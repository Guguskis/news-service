package lt.liutikas.reddit.model;

import org.springframework.context.ApplicationEvent;

import java.net.URL;
import java.time.LocalDateTime;

public class ScannedNewsEvent extends ApplicationEvent {

    private String title;
    private URL url;
    private LocalDateTime created;

    public ScannedNewsEvent(Object source) {
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
