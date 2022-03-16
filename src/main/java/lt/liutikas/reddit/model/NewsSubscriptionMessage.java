package lt.liutikas.reddit.model;

import javax.validation.constraints.NotNull;
import java.util.List;

public class NewsSubscriptionMessage {

    @NotNull
    private boolean subscribe;
    @NotNull
    private List<String> subreddits;

    public List<String> getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(List<String> subreddits) {
        this.subreddits = subreddits;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
