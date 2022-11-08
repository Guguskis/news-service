package lt.liutikas.reddit.domain.port.out.cache;

import lt.liutikas.reddit.domain.entity.core.User;

public interface AddUserPort {

    void addUser(User user);

}
