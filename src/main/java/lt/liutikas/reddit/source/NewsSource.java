package lt.liutikas.reddit.source;

import lt.liutikas.reddit.model.core.News;

import java.util.List;

public interface NewsSource {
    List<News> getNews();
}
