package lt.liutikas.reddit.api.controller;

import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.News;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/news")
public interface NewsController {

    @GetMapping
    NewsPage getAll(@Valid GetNewsRequest request);

    @GetMapping("{id}")
    News getById(@PathVariable Long id);

    @GetMapping("/channel/{channel}")
    NewsPage getChannelNews(@PathVariable Channel channel, @Valid GetNewsRequest request);

    @PostMapping
    News saveNews(@RequestBody SaveNewsRequest news);
}
