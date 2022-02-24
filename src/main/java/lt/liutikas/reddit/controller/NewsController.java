package lt.liutikas.reddit.controller;

import lt.liutikas.reddit.client.RedditClient;
import lt.liutikas.reddit.model.reddit.PageCategory;
import lt.liutikas.reddit.model.reddit.Submission;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final RedditClient redditClient;

    public NewsController(RedditClient redditClient) {
        this.redditClient = redditClient;
    }

    @RequestMapping
    public List<Submission> getNews() {
        return redditClient.getSubmissions("ukraine", PageCategory.NEW);
    }
}
