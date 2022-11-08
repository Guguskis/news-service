package lt.liutikas.reddit.adapter.in.web.query;

import lt.liutikas.reddit.domain.usecase.registeruser.RegisterUserUseCase;
import lt.liutikas.reddit.domain.usecase.unregisteruser.UnregisterUserUseCase;
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
public class UsersWebsocketAdapter implements ChannelInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(UsersWebsocketAdapter.class);

    private static final String HEADER_SIMP_SESSION_ID = "simpSessionId";

    private final RegisterUserUseCase registerUserUseCase;
    private final UnregisterUserUseCase unregisterUserUseCase;

    public UsersWebsocketAdapter(RegisterUserUseCase registerUserUseCase, UnregisterUserUseCase unregisterUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.unregisterUserUseCase = unregisterUserUseCase;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String sessionId = getSessionId(message);

        if (accessor == null) {
            LOG.error("Message header accessor cannot be null");
            return message;
        }

        if (isConnectCommand(accessor))
            registerUserUseCase.registerUser(sessionId);
        else if (isDisconnectCommand(accessor))
            unregisterUserUseCase.unregisterUser(sessionId);
        else
            throw new RuntimeException(String.format("Unknown command %s", accessor.getCommand()));

        return message;
    }

    private String getSessionId(Message<?> message) {
        return message.getHeaders().get(HEADER_SIMP_SESSION_ID, String.class);
    }

    private boolean isConnectCommand(StompHeaderAccessor accessor) {
        return StompCommand.CONNECT.equals(accessor.getCommand());
    }

    private boolean isDisconnectCommand(StompHeaderAccessor accessor) {
        return StompCommand.DISCONNECT.equals(accessor.getCommand());
    }
}
