package lt.liutikas.reddit.service;

import lt.liutikas.reddit.api.model.NewsSubscription;
import lt.liutikas.reddit.model.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NewsSubscriptionTrackerTest {

    private NewsSubscriptionTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new NewsSubscriptionTracker();
    }

    @Test
    void getSubreddits_noSessions_noSubreddits() {
        List<String> subreddits = tracker.getSubChannels("user1", Channel.REDDIT);

        assertTrue(subreddits.isEmpty());
    }

    @Test
    void getSubreddits_oneSession_noSubreddits() {
        NewsSubscription subscription = new NewsSubscription();
        subscription.setChannel(Channel.REDDIT);

        tracker.subscribe("user1", subscription);

        List<String> subreddits = tracker.getSubChannels(Channel.REDDIT);

        assertTrue(subreddits.isEmpty());
    }

    @Test
    void getSubreddits_oneSession_oneSubreddit() {
        NewsSubscription subscription = new NewsSubscription();
        subscription.setChannel(Channel.REDDIT);
        subscription.setSubChannels(List.of("combatFootage"));
        tracker.subscribe("user1", subscription);

        List<String> subreddits = tracker.getSubChannels(Channel.REDDIT);

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
    }

    @Test
    void getSubreddits_oneSession_twoSubreddits() {
        NewsSubscription subscription = new NewsSubscription();
        subscription.setChannel(Channel.REDDIT);
        subscription.setSubChannels(List.of("combatFootage", "gaming"));
        tracker.subscribe("user1", subscription);

        List<String> subreddits = tracker.getSubChannels(Channel.REDDIT);

        assertEquals(2, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
        assertTrue(subreddits.contains("gaming"));
    }

    @Test
    void getSubreddits_oneSessionMultipleSubscribes_twoSubreddits() {
        NewsSubscription subscription1 = new NewsSubscription();
        subscription1.setChannel(Channel.REDDIT);
        subscription1.setSubChannels(List.of("combatFootage"));

        NewsSubscription subscription2 = new NewsSubscription();
        subscription2.setChannel(Channel.REDDIT);
        subscription2.setSubChannels(List.of("ukraine"));

        tracker.subscribe("user1", subscription1);
        tracker.subscribe("user1", subscription2);

        List<String> subreddits = tracker.getSubChannels(Channel.REDDIT);

        assertEquals(2, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
        assertTrue(subreddits.contains("ukraine"));
    }

    @Test
    void getSubreddits_oneSessionUnsubscribed_noSubreddits() {
        NewsSubscription subscription = new NewsSubscription();
        subscription.setChannel(Channel.REDDIT);
        subscription.setSubChannels(List.of("combatFootage"));

        NewsSubscription unsubscription = new NewsSubscription();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage"));

        tracker.subscribe("user1", subscription);
        tracker.unsubscribe("user1", unsubscription);

        List<String> subreddits = tracker.getSubChannels(Channel.REDDIT);

        assertTrue(subreddits.isEmpty());
    }

    @Test
    void getSubreddits_oneSessionUnsubscribedHalf_oneSubreddit() {
        NewsSubscription subscription = new NewsSubscription();
        subscription.setChannel(Channel.REDDIT);
        subscription.setSubChannels(List.of("combatFootage", "ukraine"));

        NewsSubscription unsubscription = new NewsSubscription();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage"));

        tracker.subscribe("user1", subscription);
        tracker.unsubscribe("user1", unsubscription);

        List<String> subreddits = tracker.getSubChannels(Channel.REDDIT);

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("ukraine"));
    }

    @Test
    void getSubredditsBySession_sameSubredditDifferentLetterCase_oneSubreddit() {
        NewsSubscription subscription = new NewsSubscription();
        subscription.setChannel(Channel.REDDIT);
        subscription.setSubChannels(List.of("CombatFootage"));

        NewsSubscription unsubscription = new NewsSubscription();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatfootage"));

        tracker.subscribe("user1", subscription);
        tracker.subscribe("user2", unsubscription);

        List<String> subreddits = tracker.getSubChannels(Channel.REDDIT);

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
    }

    @Test
    void getSubreddits_twoSessionsOneUserUnsubscribedAll_oneSubreddit() {
        NewsSubscription subscription1 = new NewsSubscription();
        subscription1.setChannel(Channel.REDDIT);
        subscription1.setSubChannels(List.of("combatFootage"));

        NewsSubscription subscription2 = new NewsSubscription();
        subscription2.setChannel(Channel.REDDIT);
        subscription2.setSubChannels(List.of("combatFootage"));

        NewsSubscription unsubscription = new NewsSubscription();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage"));


        tracker.subscribe("user1", subscription1);
        tracker.subscribe("user2", subscription2);
        tracker.unsubscribe("user1", unsubscription);

        List<String> subreddits = tracker.getSubChannels(Channel.REDDIT);

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
    }

    @Test
    void getSubredditsBySession_multipleSessions_correctSessionSubredditsReturned() {
        NewsSubscription subscription1 = new NewsSubscription();
        subscription1.setChannel(Channel.REDDIT);
        subscription1.setSubChannels(List.of("combatFootage", "ukraine"));

        NewsSubscription subscription2 = new NewsSubscription();
        subscription2.setChannel(Channel.REDDIT);
        subscription2.setSubChannels(List.of("sunflowers", "war", "ukraine"));

        NewsSubscription unsubscription = new NewsSubscription();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage", "ukraine"));

        tracker.subscribe("user1", subscription1);
        tracker.subscribe("user2", subscription2);
        tracker.unsubscribe("user1", unsubscription);

        List<String> subreddits = tracker.getSubChannels("user2", Channel.REDDIT);

        assertEquals(3, subreddits.size());
        assertTrue(subreddits.contains("sunflowers"));
        assertTrue(subreddits.contains("war"));
        assertTrue(subreddits.contains("ukraine"));
    }

    @Test
    void unsubscribeSubreddits_noSubreddits_noException() {
        NewsSubscription unsubscription = new NewsSubscription();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage"));
        tracker.unsubscribe("user1", unsubscription);

        assertTrue(true);
    }

}