package lt.liutikas.reddit.adapter.out.in.web.query;

import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.domain.entity.core.Channel;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.usecase.createnews.CreateNewsUseCase;
import lt.liutikas.reddit.domain.usecase.getnews.QueryNewsUseCase;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/news")
public class QueryNewsController {

    private final QueryNewsUseCase queryNewsUseCase;
    private final CreateNewsUseCase createNewsUseCase;

    public QueryNewsController(QueryNewsUseCase queryNewsUseCase, CreateNewsUseCase createNewsUseCase) {
        this.queryNewsUseCase = queryNewsUseCase;
        this.createNewsUseCase = createNewsUseCase;
    }

    @GetMapping
    @Valid
    public NewsPage getAll(GetNewsRequest request) {
        return queryNewsUseCase.listNews(request);
    }

    @GetMapping("{id}")
    public News getById(@PathVariable Long id) {
        return queryNewsUseCase.listNews(id);
    }

    @GetMapping("/channel/{channel}")
    public NewsPage getChannelNews(@PathVariable Channel channel, @Valid GetNewsRequest request) {
        return queryNewsUseCase.listNews(channel, request);
    }

    // TODO move to separate controller?
    @PostMapping
    public News saveNews(@RequestBody SaveNewsRequest news) {
        return createNewsUseCase.createNews(news);
    }

}
