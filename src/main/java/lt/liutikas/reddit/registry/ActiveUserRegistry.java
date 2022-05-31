package lt.liutikas.reddit.registry;

import lt.liutikas.reddit.model.core.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ActiveUserRegistry {

    private final List<User> activeUsers = new ArrayList<>();

    public List<User> getActiveUsers() {
        return activeUsers;
    }

    public void addUser(User user) {
        activeUsers.add(user);
    }

    public void removeUserBySessionId(String sessionId) {
        activeUsers.removeIf(user -> user.getSessionId().equals(sessionId)); // fixme somehow activeUsers ended up with null user
    }
}
