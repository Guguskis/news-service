package lt.liutikas.reddit.model.event;

import lt.liutikas.reddit.api.model.SubscriptionAction;

public class NewsSubscriptionEvent {

    private SubscriptionAction subscriptionAction;
    private String sessionId;

    public SubscriptionAction getSubscriptionAction() {
        return subscriptionAction;
    }

    public void setSubscriptionAction(SubscriptionAction subscriptionAction) {
        this.subscriptionAction = subscriptionAction;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
