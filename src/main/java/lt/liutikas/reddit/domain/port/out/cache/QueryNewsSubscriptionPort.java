package lt.liutikas.reddit.domain.port.out.cache;

import lt.liutikas.reddit.domain.entity.core.News;

public interface QueryNewsSubscriptionPort {

    boolean isSubscribed(String sessionId, News news);

}
