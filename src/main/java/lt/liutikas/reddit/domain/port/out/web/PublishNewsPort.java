package lt.liutikas.reddit.domain.port.out.web;

import lt.liutikas.reddit.domain.entity.core.News;

public interface PublishNewsPort {

    void publishNews(String sessionId, News news);

}
