package lt.liutikas.reddit.domain.entity.core;

public class Subscription {

    private Channel channel;
    private String subChannel;

    public Subscription() {
    }

    public Subscription(Channel channel, String subChannel) {
        this.channel = channel;
        this.subChannel = subChannel;
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
