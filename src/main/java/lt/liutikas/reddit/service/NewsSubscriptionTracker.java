package lt.liutikas.reddit.service;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class NewsSubscriptionTracker {

    // one subreddit -> many sessionId
    private final Map<String, Set<String>> subscriptions = new HashMap<>();

    public void subscribeSubreddits(String sessionId, List<String> subreddits) {
        synchronized (subscriptions) {
            for (String subreddit : subreddits) {
                subscriptions.computeIfAbsent(subreddit, k -> new HashSet<>()).add(sessionId);
            }
        }
    }

    public void unsubscribeSubreddits(String sessionId, List<String> subreddits) {
        synchronized (subscriptions) {
            for (String subreddit : subreddits) {
                subscriptions.computeIfPresent(subreddit, (sub, sess) -> {
                    sess.remove(sessionId);
                    return sess;
                });
            }
        }
    }

    public void unsubscribeSubreddits(String sessionId) {
        synchronized (subscriptions) {
            subscriptions.values().forEach(s -> s.remove(sessionId));
        }
    }

    public List<String> getSubreddits(String sessionId) {
        return subscriptions.entrySet().stream()
                .filter(e -> e.getValue().contains(sessionId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> getSubreddits() {
        return subscriptions.entrySet().stream()
                .filter(e -> e.getValue().size() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
