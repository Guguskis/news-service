package lt.liutikas.reddit.model.api;

import lt.liutikas.reddit.model.Channel;

import java.net.URL;

public class SaveNewsRequest {

    private String title;
    private URL url;
    private Channel channel;
    private String subreddit;

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

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }
}
