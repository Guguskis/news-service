package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.model.NewsPage;
import lt.liutikas.reddit.model.PaginationQuery;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @MessageMapping("/news")
    @SendTo("/topic/news")
    public String news(String message) {

        System.out.println(message);
        return "Hello, " + message + "!";
    }

    @GetMapping
    public NewsPage news(@Valid PaginationQuery query) {
        return newsService.getAll(query.pageRequest());
    }

}
