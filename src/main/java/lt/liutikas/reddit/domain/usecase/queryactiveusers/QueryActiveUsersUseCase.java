package lt.liutikas.reddit.domain.usecase.queryactiveusers;

import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.domain.port.out.persistence.QueryActiveUsersPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryActiveUsersUseCase {

    final QueryActiveUsersPort queryActiveUsersPort;

    public QueryActiveUsersUseCase(QueryActiveUsersPort queryActiveUsersPort) {
        this.queryActiveUsersPort = queryActiveUsersPort;
    }

    public List<User> listActiveUsers() {
        return queryActiveUsersPort.listActiveUsers();
    }
}
