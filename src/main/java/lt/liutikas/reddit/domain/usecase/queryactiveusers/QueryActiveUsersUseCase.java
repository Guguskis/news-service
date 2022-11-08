package lt.liutikas.reddit.domain.usecase.queryactiveusers;

import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.domain.port.out.cache.QueryUsersPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryActiveUsersUseCase {

    final QueryUsersPort queryUsersPort;

    public QueryActiveUsersUseCase(QueryUsersPort queryUsersPort) {
        this.queryUsersPort = queryUsersPort;
    }

    public List<User> listActiveUsers() {
        return queryUsersPort.listActiveUsers();
    }
}
