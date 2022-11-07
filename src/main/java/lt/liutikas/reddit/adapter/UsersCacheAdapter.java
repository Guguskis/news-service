package lt.liutikas.reddit.adapter;

import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.domain.port.out.persistence.QueryActiveUsersPort;
import lt.liutikas.reddit.registry.ActiveUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersCacheAdapter implements QueryActiveUsersPort {

    private final ActiveUserRegistry activeUserRegistry;

    public UsersCacheAdapter(ActiveUserRegistry activeUserRegistry) {
        this.activeUserRegistry = activeUserRegistry;
    }

    @Override
    public List<User> listActiveUsers() {
        return activeUserRegistry.getActiveUsers();
    }
}
