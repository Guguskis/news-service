package lt.liutikas.reddit.source;

import lt.liutikas.reddit.domain.entity.core.News;

import java.util.List;

public interface NewsSource {
    List<News> getNews();
}
