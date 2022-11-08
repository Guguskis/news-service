package lt.liutikas.reddit.domain.port.out.cache;

import lt.liutikas.reddit.domain.entity.core.User;

import java.util.List;

public interface QueryUsersPort {

    List<User> listActiveUsers();

}
