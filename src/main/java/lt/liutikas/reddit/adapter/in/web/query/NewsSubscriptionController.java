package lt.liutikas.reddit.adapter.in.web.query;

import lt.liutikas.reddit.api.model.SubscriptionAction;
import lt.liutikas.reddit.domain.usecase.updatenewssubscription.UpdateNewsSubscriptionUseCase;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsSubscriptionController {
    private final UpdateNewsSubscriptionUseCase updateNewsSubscriptionUseCase;

    public NewsSubscriptionController(UpdateNewsSubscriptionUseCase updateNewsSubscriptionUseCase) {
        this.updateNewsSubscriptionUseCase = updateNewsSubscriptionUseCase;
    }

    @MessageMapping("/queue/news")
    public void handleNewsSubscription(@Header("simpSessionId") String sessionId, SubscriptionAction action) {
        updateNewsSubscriptionUseCase.updateNewsSubscription(sessionId, action);
    }
}
