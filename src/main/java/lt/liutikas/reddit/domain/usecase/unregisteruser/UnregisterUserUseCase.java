package lt.liutikas.reddit.domain.usecase.unregisteruser;

import lt.liutikas.reddit.domain.port.in.cache.RemoveUserPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UnregisterUserUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(UnregisterUserUseCase.class);

    private final RemoveUserPort removeUserPort;

    public UnregisterUserUseCase(RemoveUserPort removeUserPort) {
        this.removeUserPort = removeUserPort;
    }

    public void unregisterUser(String sessionId) {
        removeUserPort.removeUserBySessionId(sessionId);
        LOG.info("User disconnected {\"sessionId\": \"{}\"}", sessionId);
    }

}


