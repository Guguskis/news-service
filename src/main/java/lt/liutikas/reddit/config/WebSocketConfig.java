package lt.liutikas.reddit.config;

import lt.liutikas.reddit.adapter.in.web.query.UsersWebsocketAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebMvc
@Controller
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UsersWebsocketAdapter usersWebsocketAdapter;

    public WebSocketConfig(UsersWebsocketAdapter usersWebsocketAdapter) {
        this.usersWebsocketAdapter = usersWebsocketAdapter;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/news").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(usersWebsocketAdapter);
    }
}