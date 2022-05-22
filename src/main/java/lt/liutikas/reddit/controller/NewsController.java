package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.api.GetNewsRequest;
import lt.liutikas.reddit.model.api.NewsPage;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public NewsPage getAll(@Valid GetNewsRequest request) {
        return newsService.getNews(request);
    }

    @GetMapping("{channel}")
    public NewsPage getChannelNews(@PathVariable Channel channel, @Valid GetNewsRequest request) {
        return newsService.getNews(channel, request);
    }

}
