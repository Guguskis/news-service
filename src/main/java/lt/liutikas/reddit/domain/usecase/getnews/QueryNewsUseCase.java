package lt.liutikas.reddit.domain.usecase.getnews;

import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.domain.entity.core.Channel;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.port.out.persistence.QueryNewsPort;
import org.springframework.stereotype.Service;

@Service
public class QueryNewsUseCase {

    final QueryNewsPort queryNewsPort;

    public QueryNewsUseCase(QueryNewsPort queryNewsPort) {
        this.queryNewsPort = queryNewsPort;
    }

    public News listNews(Long id) {
        return queryNewsPort.findOne(id);
    }

    public NewsPage listNews(GetNewsRequest request) {
        return queryNewsPort.findMany(request);
    }

    public NewsPage listNews(Channel channel, GetNewsRequest request) {
        return queryNewsPort.findMany(channel, request);
    }
}
