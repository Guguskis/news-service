package lt.liutikas.reddit.model;

import javax.validation.constraints.NotNull;
import java.util.List;

public class RedditSubscriptionMessage {

    @NotNull
    private SubscriptionAction action;
    @NotNull
    private List<String> subreddits;

    public List<String> getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(List<String> subreddits) {
        this.subreddits = subreddits;
    }

    public SubscriptionAction getAction() {
        return action;
    }

    public void setAction(SubscriptionAction action) {
        this.action = action;
    }
}
