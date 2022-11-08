package lt.liutikas.reddit.adapter;

import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.domain.port.in.cache.AddUserPort;
import lt.liutikas.reddit.domain.port.in.cache.RemoveUserPort;
import lt.liutikas.reddit.domain.port.out.cache.QueryUsersPort;
import lt.liutikas.reddit.registry.ActiveUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersCacheAdapter implements QueryUsersPort, AddUserPort, RemoveUserPort {

    private final ActiveUserRegistry activeUserRegistry;

    public UsersCacheAdapter(ActiveUserRegistry activeUserRegistry) {
        this.activeUserRegistry = activeUserRegistry;
    }

    @Override
    public List<User> listActiveUsers() {
        return activeUserRegistry.getActiveUsers();
    }

    @Override
    public void addUser(User user) {
        activeUserRegistry.addUser(user);
    }

    @Override
    public void removeUserBySessionId(String sessionId) {
        activeUserRegistry.removeUserBySessionId(sessionId);
    }
}
