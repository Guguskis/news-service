package lt.liutikas.reddit.domain.usecase.registeruser;

import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.domain.port.in.cache.AddUserPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUserUseCase.class);

    private final AddUserPort addUserPort;

    public RegisterUserUseCase(AddUserPort addUserPort) {
        this.addUserPort = addUserPort;
    }

    public void registerUser(String sessionId) {
        User user = new User();
        user.setSessionId(sessionId);
        addUserPort.addUser(user);
        LOG.info("User connected {\"sessionId\": \"{}\"}", sessionId);
    }

}


