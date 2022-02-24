package lt.liutikas.reddit.model;

import org.springframework.context.ApplicationEvent;

import java.net.URL;

public class NewsEvent extends ApplicationEvent {

    private String headline;
    private URL url;

    public NewsEvent(Object source) {
        super(source);
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

}
