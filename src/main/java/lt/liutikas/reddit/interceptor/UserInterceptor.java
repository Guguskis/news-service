package lt.liutikas.reddit.interceptor;

import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.domain.port.out.cache.AddUserPort;
import lt.liutikas.reddit.domain.port.out.cache.RemoveUserPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class UserInterceptor implements ChannelInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(UserInterceptor.class);

    private final AddUserPort addUserPort;
    private final RemoveUserPort removeUserPort;

    public UserInterceptor(AddUserPort addUserPort, RemoveUserPort removeUserPort) {
        this.addUserPort = addUserPort;
        this.removeUserPort = removeUserPort;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            LOG.warn("Message header accessor is null");
            return message;
        }

        if (isConnectCommand(accessor)) {
            String sessionId = getSessionId(message);
            User user = new User();
            user.setSessionId(sessionId);
            addUserPort.addUser(user);
            LOG.info("User connected {\"sessionId\": \"{}\"}", sessionId);
        } else if (isDisconnectCommand(accessor)) {
            String sessionId = getSessionId(message);
            removeUserPort.removeUserBySessionId(sessionId);
            LOG.info("User disconnected {\"sessionId\": \"{}\"}", sessionId);
        }

        return message;
    }

    private String getSessionId(Message<?> message) {
        return message.getHeaders().get("simpSessionId", String.class);
    }

    private boolean isConnectCommand(StompHeaderAccessor accessor) {
        return StompCommand.CONNECT.equals(accessor.getCommand());
    }

    private boolean isDisconnectCommand(StompHeaderAccessor accessor) {
        return StompCommand.DISCONNECT.equals(accessor.getCommand());
    }
}


