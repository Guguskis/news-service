package lt.liutikas.reddit.domain.port.out.queryactiveusers;

import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.registry.ActiveUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersCacheAdapter implements QueryActiveUsersPort {

    private ActiveUserRegistry activeUserRegistry;

    @Override
    public List<User> listActiveUsers() {
        return activeUserRegistry.getActiveUsers();
    }
}
