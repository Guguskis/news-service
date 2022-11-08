package lt.liutikas.reddit.domain.usecase.createnews;

import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.domain.port.in.CreateNewsPort;
import lt.liutikas.reddit.domain.port.out.cache.QueryNewsSubscriptionPort;
import lt.liutikas.reddit.domain.port.out.cache.QueryUsersPort;
import lt.liutikas.reddit.domain.port.out.web.PublishNewsPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateNewsUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewsUseCase.class);

    private final NewsAssembler newsAssembler;

    private final PublishNewsPort publishNewsPort;
    private final QueryUsersPort queryUsersPort;
    private final QueryNewsSubscriptionPort queryNewsSubscriptionPort;
    private final CreateNewsPort createNewsPort;

    public CreateNewsUseCase(NewsAssembler newsAssembler, PublishNewsPort publishNewsPort, QueryUsersPort queryUsersPort, QueryNewsSubscriptionPort queryNewsSubscriptionPort, CreateNewsPort createNewsPort) {
        this.newsAssembler = newsAssembler;
        this.publishNewsPort = publishNewsPort;
        this.queryUsersPort = queryUsersPort;
        this.queryNewsSubscriptionPort = queryNewsSubscriptionPort;
        this.createNewsPort = createNewsPort;
    }

    public News createNews(SaveNewsRequest request) {
        News news = newsAssembler.assembleNews(request);
        news = createNewsPort.create(news);

        LOG.info("Saving news { \"title\": {} }", news.getTitle());

        for (User user : queryUsersPort.listActiveUsers()) {
            if (queryNewsSubscriptionPort.isSubscribed(user.getSessionId(), news)) {
                publishNewsPort.publishNews(user.getSessionId(), news);
            }
        }

        LOG.info("Saved news { \"title\": {} }", news.getTitle());

        return news;
    }


}
