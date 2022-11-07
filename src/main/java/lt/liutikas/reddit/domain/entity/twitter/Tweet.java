package lt.liutikas.reddit.domain.entity.twitter;

import java.net.URL;
import java.time.LocalDateTime;

public class Tweet {

    private String text;
    private URL url;
    private LocalDateTime createdAt;
    private TweetSubChannelType subChannelType;
    private String user;
    private String keyword;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TweetSubChannelType subChannelType() {
        return subChannelType;
    }

    public void setSubChannelType(TweetSubChannelType subChannelType) {
        this.subChannelType = subChannelType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
