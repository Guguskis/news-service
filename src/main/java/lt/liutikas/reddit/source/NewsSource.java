package lt.liutikas.reddit.source;

import lt.liutikas.reddit.model.News;

import java.util.List;

public interface NewsSource {
    List<News> getNews();
}
