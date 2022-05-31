package lt.liutikas.reddit.api.model;

import lt.liutikas.reddit.model.core.Channel;

import java.net.URL;

public class SaveNewsRequest {

    private String title;
    private URL url;
    private Channel channel;
    private String subChannel;

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

    public String getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(String subChannel) {
        this.subChannel = subChannel;
    }
}
