package lt.liutikas.reddit.service;

import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.NewsSubscription;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class NewsSubscriptionTracker {

    // one subreddit -> many sessionId
    private final Map<String, Set<String>> subChannels = new HashMap<>();

    public void subscribe(String sessionId, NewsSubscription subscription) {
        synchronized (subChannels) {
            for (String subreddit : cleanSubChannels(subscription.getSubChannels())) {
                subChannels.computeIfAbsent(subreddit.toLowerCase(), k -> new HashSet<>()).add(sessionId);
            }
        }
    }

    public void unsubscribe(String sessionId, NewsSubscription subscription) {
        synchronized (subChannels) {
            for (String subChannel : cleanSubChannels(subscription.getSubChannels())) {
                subChannels.computeIfPresent(subChannel.toLowerCase(), (sub, sess) -> {
                    sess.remove(sessionId);
                    return sess;
                });
            }
        }
    }

    public void unsubscribe(String sessionId, Channel channel) {
        synchronized (subChannels) {
            subChannels.values().forEach(s -> s.remove(sessionId));
        }
    }

    public List<String> getSubChannels(String sessionId, Channel channel) {
        return subChannels.entrySet().stream()
                .filter(e -> e.getValue().contains(sessionId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> getSubChannels(Channel channel) {
        return subChannels.entrySet().stream()
                .filter(e -> e.getValue().size() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<String> cleanSubChannels(List<String> subreddits) {
        return subreddits.stream()
                .map(String::toLowerCase)
                .distinct().collect(Collectors.toList());
    }
}
