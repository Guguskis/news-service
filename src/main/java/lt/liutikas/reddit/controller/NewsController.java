package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.api.GetNewsRequest;
import lt.liutikas.reddit.model.api.NewsPage;
import lt.liutikas.reddit.model.api.SaveNewsRequest;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
    public News getById(@PathVariable Long id) {
        return newsService.getNews(id);
    }

    @GetMapping("/channel/{channel}")
    public NewsPage getChannelNews(@PathVariable Channel channel, @Valid GetNewsRequest request) {
        return newsService.getNews(channel, request);
    }

    @PostMapping
    public News saveNews(@RequestBody SaveNewsRequest news) {
        return newsService.saveNews(news);
    }

}
