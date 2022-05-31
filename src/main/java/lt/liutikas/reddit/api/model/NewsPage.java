package lt.liutikas.reddit.api.model;

import lt.liutikas.reddit.model.core.News;

import java.util.List;

public class NewsPage extends PaginationResponse {

    private List<News> news;

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
