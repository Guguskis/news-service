package lt.liutikas.reddit.model.event;

import lt.liutikas.reddit.api.model.NewsSubscription;

public class NewsSubscriptionEvent {

    private NewsSubscription newsSubscription;
    private String sessionId;

    public NewsSubscription getNewsSubscription() {
        return newsSubscription;
    }

    public void setNewsSubscription(NewsSubscription newsSubscription) {
        this.newsSubscription = newsSubscription;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
