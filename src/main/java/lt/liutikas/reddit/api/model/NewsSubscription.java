package lt.liutikas.reddit.api.model;

import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.SubscriptionAction;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class NewsSubscription {

    @NotNull
    private SubscriptionAction action;
    @NotNull
    private Channel channel;
    @NotNull
    private List<String> subChannels = new ArrayList<>();

    public List<String> getSubChannels() {
        return subChannels;
    }

    public void setSubChannels(List<String> subChannels) {
        this.subChannels = subChannels;
    }

    public SubscriptionAction getAction() {
        return action;
    }

    public void setAction(SubscriptionAction action) {
        this.action = action;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
