package lt.liutikas.reddit.domain.port.out.queryactiveusers;

import lt.liutikas.reddit.domain.entity.core.User;

import java.util.List;

public interface QueryActiveUsersPort {

    List<User> listActiveUsers();

}
