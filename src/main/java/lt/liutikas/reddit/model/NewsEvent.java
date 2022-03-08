package lt.liutikas.reddit.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.context.ApplicationEvent;

import java.net.URL;
import java.time.LocalDateTime;

public class NewsEvent extends ApplicationEvent {

    private String title;
    private URL url;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime created;

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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
