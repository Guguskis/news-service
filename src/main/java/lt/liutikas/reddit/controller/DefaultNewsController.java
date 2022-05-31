package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.api.controller.NewsController;
import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.model.core.Channel;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultNewsController implements NewsController {

    private final NewsService newsService;

    public DefaultNewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public NewsPage getAll(GetNewsRequest request) {
        return newsService.getNews(request);
    }

    @Override
    public News getById(Long id) {
        return newsService.getNews(id);
    }

    @Override
    public NewsPage getChannelNews(Channel channel, GetNewsRequest request) {
        return newsService.getNews(channel, request);
    }

    @Override
    public News saveNews(SaveNewsRequest news) {
        return newsService.saveNews(news);
    }

}
