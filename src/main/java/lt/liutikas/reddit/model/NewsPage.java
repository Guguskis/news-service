package lt.liutikas.reddit.model;

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
