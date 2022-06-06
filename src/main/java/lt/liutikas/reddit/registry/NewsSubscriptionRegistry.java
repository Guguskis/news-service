package lt.liutikas.reddit.registry;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.model.core.Channel;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.core.Subscription;
import lt.liutikas.reddit.model.core.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NewsSubscriptionRegistry {

    private final Map<String, List<Subscription>> subscriptions = new HashMap<>();

    public boolean isSubscribed(User user, News news) {
        List<String> userSubChannels = getSubChannels(user.getSessionId(), news.getChannel());
        return userSubChannels.contains(news.getSubChannel());
    }

    public void subscribe(String sessionId, SubscriptionAction action) {
        synchronized (subscriptions) {
            Channel channel = action.getChannel();
            List<String> subChannels = cleanSubChannels(action.getSubChannels());
            List<Subscription> userSubscriptions = getUserSubscriptions(sessionId, channel);
            addSubscriptions(channel, subChannels, userSubscriptions);
            subscriptions.replace(sessionId, userSubscriptions);
        }
    }

    public void unsubscribe(String sessionId, Channel channel) {
        synchronized (subscriptions) {
            List<Subscription> userSubscriptions = getUserSubscriptions(sessionId, channel);
            userSubscriptions.removeIf(s -> s.getChannel().equals(channel));
        }
    }

    public void unsubscribe(String sessionId, SubscriptionAction action) {
        synchronized (subscriptions) {
            List<Subscription> userSubscriptions = getUserSubscriptions(sessionId, action.getChannel());
            removeSubscriptions(userSubscriptions, action.getSubChannels());
            subscriptions.replace(sessionId, userSubscriptions);
        }
    }

    public List<String> getSubChannels(String sessionId, Channel channel) {
        return subscriptions.entrySet().stream()
                .filter(e -> e.getKey().equals(sessionId))
                .flatMap(e -> e.getValue().stream())
                .filter(s -> s.getChannel().equals(channel))
                .map(Subscription::getSubChannel)
                .collect(Collectors.toList());
    }

    public List<String> getSubChannels(Channel channel) {
        return subscriptions.entrySet().stream()
                .flatMap(e -> e.getValue().stream())
                .filter(s -> s.getChannel().equals(channel))
                .map(Subscription::getSubChannel)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> cleanSubChannels(List<String> subChannels) {
        return subChannels.stream()
                .map(String::toLowerCase)
                .distinct().collect(Collectors.toList());
    }

    private List<Subscription> getUserSubscriptions(String sessionId, Channel channel) {
        subscriptions.computeIfAbsent(sessionId, k -> new ArrayList<>());
        return subscriptions.get(sessionId).stream()
                .filter(s -> s.getChannel().equals(channel))
                .collect(Collectors.toList());
    }

    private void addSubscriptions(Channel channel, List<String> subChannels, List<Subscription> subscriptions) {
        for (String subChannel : subChannels) {
            if (containsSubChannel(subscriptions, subChannel))
                continue;

            Subscription subscription = new Subscription(channel, subChannel);
            subscriptions.add(subscription);
        }
    }

    private void removeSubscriptions(List<Subscription> userSubscriptions, List<String> subChannels) {
        for (String subChannel : cleanSubChannels(subChannels)) {
            userSubscriptions.removeIf(s -> s.getSubChannel().equals(subChannel));
        }
    }

    private boolean containsSubChannel(List<Subscription> subscriptions, String subChannel) {
        return subscriptions.stream()
                .anyMatch(s -> s.getSubChannel().equals(subChannel));
    }
}
