package lt.liutikas.reddit.domain.port.in.cache;

import lt.liutikas.reddit.domain.entity.core.User;

public interface AddUserPort {

    void addUser(User user);

}
