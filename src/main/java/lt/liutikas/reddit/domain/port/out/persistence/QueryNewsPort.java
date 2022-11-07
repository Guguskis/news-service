package lt.liutikas.reddit.domain.port.out.persistence;

import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.domain.entity.core.Channel;
import lt.liutikas.reddit.domain.entity.core.News;

public interface QueryNewsPort {

    News findOne(Long id);

    // TODO return List<News> and do mapping to page later?
    NewsPage findMany(GetNewsRequest request);

    NewsPage findMany(Channel channel, GetNewsRequest request);
}
