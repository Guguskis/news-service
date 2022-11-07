package lt.liutikas.reddit.api.model;

import lt.liutikas.reddit.domain.entity.core.Channel;
import lt.liutikas.reddit.domain.entity.core.SubscriptionActionType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionAction {

    @NotNull
    private SubscriptionActionType actionType;
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

    public SubscriptionActionType getActionType() {
        return actionType;
    }

    public void setActionType(SubscriptionActionType actionType) {
        this.actionType = actionType;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
