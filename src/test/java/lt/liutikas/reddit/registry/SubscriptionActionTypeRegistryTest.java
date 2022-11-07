package lt.liutikas.reddit.registry;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.domain.entity.core.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionActionTypeRegistryTest {

    private NewsSubscriptionRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new NewsSubscriptionRegistry();
    }

    @Test
    void getSubreddits_noSessions_noSubreddits() {
        List<String> subreddits = registry.getSubChannels("user1", Channel.REDDIT);

        assertTrue(subreddits.isEmpty());
    }

    @Test
    void getSubreddits_oneSession_noSubreddits() {
        SubscriptionAction subscription = new SubscriptionAction();
        subscription.setChannel(Channel.REDDIT);

        registry.subscribe("user1", subscription);

        List<String> subreddits = registry.getSubChannels(Channel.REDDIT);

        assertTrue(subreddits.isEmpty());
    }

    @Test
    void getSubreddits_oneSession_oneSubreddit() {
        SubscriptionAction subscription = new SubscriptionAction();
        subscription.setChannel(Channel.REDDIT);
        subscription.setSubChannels(List.of("combatFootage"));
        registry.subscribe("user1", subscription);

        List<String> subreddits = registry.getSubChannels(Channel.REDDIT);

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
    }

    @Test
    void getSubreddits_oneSession_twoSubreddits() {
        SubscriptionAction subscription = new SubscriptionAction();
        subscription.setChannel(Channel.REDDIT);
        subscription.setSubChannels(List.of("combatFootage", "gaming"));
        registry.subscribe("user1", subscription);

        List<String> subreddits = registry.getSubChannels(Channel.REDDIT);

        assertEquals(2, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
        assertTrue(subreddits.contains("gaming"));
    }

    @Test
    void getSubreddits_oneSessionMultipleSubscribes_twoSubreddits() {
        SubscriptionAction subscription1 = new SubscriptionAction();
        subscription1.setChannel(Channel.REDDIT);
        subscription1.setSubChannels(List.of("combatFootage"));

        SubscriptionAction subscription2 = new SubscriptionAction();
        subscription2.setChannel(Channel.REDDIT);
        subscription2.setSubChannels(List.of("ukraine"));

        registry.subscribe("user1", subscription1);
        registry.subscribe("user1", subscription2);

        List<String> subreddits = registry.getSubChannels(Channel.REDDIT);

        assertEquals(2, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
        assertTrue(subreddits.contains("ukraine"));
    }

    @Test
    void getSubreddits_oneSessionUnsubscribed_noSubreddits() {
        SubscriptionAction subscription = new SubscriptionAction();
        subscription.setChannel(Channel.REDDIT);
        subscription.setSubChannels(List.of("combatFootage"));

        SubscriptionAction unsubscription = new SubscriptionAction();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage"));

        registry.subscribe("user1", subscription);
        registry.unsubscribe("user1", unsubscription);

        List<String> subreddits = registry.getSubChannels(Channel.REDDIT);

        assertTrue(subreddits.isEmpty());
    }

    @Test
    void getSubreddits_oneSessionUnsubscribedHalf_oneSubreddit() {
        SubscriptionAction subscription = new SubscriptionAction();
        subscription.setChannel(Channel.REDDIT);
        subscription.setSubChannels(List.of("combatFootage", "ukraine"));

        SubscriptionAction unsubscription = new SubscriptionAction();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage"));

        registry.subscribe("user1", subscription);
        registry.unsubscribe("user1", unsubscription);

        List<String> subreddits = registry.getSubChannels(Channel.REDDIT);

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("ukraine"));
    }

    @Test
    void getSubredditsBySession_sameSubredditDifferentLetterCase_oneSubreddit() {
        SubscriptionAction subscriptionUpperCase = new SubscriptionAction();
        subscriptionUpperCase.setChannel(Channel.REDDIT);
        subscriptionUpperCase.setSubChannels(List.of("CombatFootage"));

        SubscriptionAction subscriptionLowerCase = new SubscriptionAction();
        subscriptionLowerCase.setChannel(Channel.REDDIT);
        subscriptionLowerCase.setSubChannels(List.of("combatfootage"));

        registry.subscribe("user1", subscriptionUpperCase);
        registry.subscribe("user2", subscriptionLowerCase);

        List<String> subreddits = registry.getSubChannels(Channel.REDDIT);

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
    }

    @Test
    void getSubreddits_twoSessionsOneUserUnsubscribedAll_oneSubreddit() {
        SubscriptionAction subscription1 = new SubscriptionAction();
        subscription1.setChannel(Channel.REDDIT);
        subscription1.setSubChannels(List.of("combatFootage"));

        SubscriptionAction subscription2 = new SubscriptionAction();
        subscription2.setChannel(Channel.REDDIT);
        subscription2.setSubChannels(List.of("combatFootage"));

        SubscriptionAction unsubscription = new SubscriptionAction();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage"));


        registry.subscribe("user1", subscription1);
        registry.subscribe("user2", subscription2);
        registry.unsubscribe("user1", unsubscription);

        List<String> subreddits = registry.getSubChannels(Channel.REDDIT);

        assertEquals(1, subreddits.size());
        assertTrue(subreddits.contains("combatfootage"));
    }

    @Test
    void getSubredditsBySession_multipleSessions_correctSessionSubredditsReturned() {
        SubscriptionAction subscription1 = new SubscriptionAction();
        subscription1.setChannel(Channel.REDDIT);
        subscription1.setSubChannels(List.of("combatFootage", "ukraine"));

        SubscriptionAction subscription2 = new SubscriptionAction();
        subscription2.setChannel(Channel.REDDIT);
        subscription2.setSubChannels(List.of("sunflowers", "war", "ukraine"));

        SubscriptionAction unsubscription = new SubscriptionAction();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage", "ukraine"));

        registry.subscribe("user1", subscription1);
        registry.subscribe("user2", subscription2);
        registry.unsubscribe("user1", unsubscription);

        List<String> subreddits = registry.getSubChannels("user2", Channel.REDDIT);

        assertEquals(3, subreddits.size());
        assertTrue(subreddits.contains("sunflowers"));
        assertTrue(subreddits.contains("war"));
        assertTrue(subreddits.contains("ukraine"));
    }

    @Test
    void unsubscribeSubreddits_noSubreddits_noException() {
        SubscriptionAction unsubscription = new SubscriptionAction();
        unsubscription.setChannel(Channel.REDDIT);
        unsubscription.setSubChannels(List.of("combatFootage"));
        registry.unsubscribe("user1", unsubscription);

        assertTrue(true);
    }

}