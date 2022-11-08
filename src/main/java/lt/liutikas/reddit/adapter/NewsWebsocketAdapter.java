package lt.liutikas.reddit.adapter;

import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.port.out.web.PublishNewsPort;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NewsWebsocketAdapter implements PublishNewsPort {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public NewsWebsocketAdapter(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void publishNews(String sessionId, News news) {
        MessageHeaders headers = getMessageHeaders(sessionId);
        simpMessagingTemplate.convertAndSendToUser(sessionId, "/topic/news", news, headers);
    }

    private MessageHeaders getMessageHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
