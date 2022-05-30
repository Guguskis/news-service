package lt.liutikas.reddit.api;

import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.api.GetNewsRequest;
import lt.liutikas.reddit.model.api.NewsPage;
import lt.liutikas.reddit.model.api.SaveNewsRequest;
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
