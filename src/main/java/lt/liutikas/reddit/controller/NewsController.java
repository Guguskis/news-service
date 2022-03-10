package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.model.NewsPage;
import lt.liutikas.reddit.model.PaginationQuery;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
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
    public NewsPage getAll(@Valid PaginationQuery query) {
        return newsService.getAll(query.pageRequest());
    }

}
