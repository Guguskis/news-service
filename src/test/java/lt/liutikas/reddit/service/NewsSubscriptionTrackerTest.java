package lt.liutikas.reddit.service;

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
        List<String> subreddits = tracker.getSubreddits("user1");

        assertTrue(subreddits.isEmpty());
    }

    @Test
    void getSubreddits_oneSession_noSubreddits() {
        tracker.subscribeSubreddits("user1", List.of());

        List<String> subreddits = tracker.getSubreddits();

        assertTrue(subreddits.isEmpty());
    }

    @Test
    void getSubreddits_oneSession_oneSubreddit() {
        tracker.subscribeSubreddits("user1", List.of("combatFootage"));

        List<String> subreddits = tracker.getSubreddits();

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("combatFootage"));
    }

    @Test
    void getSubreddits_twoSessions_twoSubreddits() {
        tracker.subscribeSubreddits("user1", List.of("combatFootage"));
        tracker.subscribeSubreddits("user2", List.of("ukraine"));

        List<String> subreddits = tracker.getSubreddits();

        assertEquals(2, subreddits.size());
        assertTrue(subreddits.contains("combatFootage"));
        assertTrue(subreddits.contains("ukraine"));
    }

    @Test
    void getSubreddits_oneSessionUnsubscribed_noSubreddits() {
        tracker.subscribeSubreddits("user1", List.of("combatFootage"));
        tracker.unsubscribeSubreddits("user1", List.of("combatFootage"));

        List<String> subreddits = tracker.getSubreddits();

        assertTrue(subreddits.isEmpty());
    }

    @Test
    void getSubreddits_oneSessionUnsubscribedHalf_oneSubreddit() {
        tracker.subscribeSubreddits("user1", List.of("combatFootage", "ukraine"));
        tracker.unsubscribeSubreddits("user1", List.of("combatFootage"));

        List<String> subreddits = tracker.getSubreddits();

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("ukraine"));
    }

    @Test
    void getSubreddits_twoSessionsOneUserUnsubscribedAll_oneSubreddit() {
        tracker.subscribeSubreddits("user1", List.of("combatFootage"));
        tracker.subscribeSubreddits("user2", List.of("combatFootage"));
        tracker.unsubscribeSubreddits("user1", List.of("combatFootage"));

        List<String> subreddits = tracker.getSubreddits();

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("combatFootage"));
    }

    @Test
    void getSubredditsBySession_multipleSessions_correctSessionSubredditsReturned() {
        tracker.subscribeSubreddits("user1", List.of("combatFootage", "ukraine"));
        tracker.subscribeSubreddits("user2", List.of("sunflowers", "war", "ukraine"));
        tracker.unsubscribeSubreddits("user1", List.of("combatFootage", "ukraine"));

        List<String> subreddits = tracker.getSubreddits("user2");

        assertEquals(3, subreddits.size());
        assertTrue(subreddits.contains("sunflowers"));
        assertTrue(subreddits.contains("war"));
        assertTrue(subreddits.contains("ukraine"));
    }

}