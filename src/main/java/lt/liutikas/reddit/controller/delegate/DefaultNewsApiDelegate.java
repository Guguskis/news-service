package lt.liutikas.reddit.controller.delegate;

import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.model.core.Channel;
import lt.liutikas.reddit.openapi.api.NewsApiDelegate;
import lt.liutikas.reddit.openapi.model.CreateNewsRequest;
import lt.liutikas.reddit.openapi.model.News;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultNewsApiDelegate implements NewsApiDelegate {

    private final NewsService newsService;
    private final NewsAssembler newsAssembler;

    public DefaultNewsApiDelegate(NewsService newsService, NewsAssembler newsAssembler) {
        this.newsService = newsService;
        this.newsAssembler = newsAssembler;
    }

    @Override
    public ResponseEntity<lt.liutikas.reddit.openapi.model.NewsPage> listNews(List<String> subChannels,
                                                                              Integer pageToken,
                                                                              Integer pageSize) {
        GetNewsRequest request = new GetNewsRequest(subChannels, pageToken, pageSize);
        lt.liutikas.reddit.openapi.model.NewsPage newsPage = newsService.listNews(request);
        return new ResponseEntity<>(newsPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<News> createNews(CreateNewsRequest createNewsRequest) {
        News news = newsService.createNews(createNewsRequest);
        return new ResponseEntity<>(news, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<News> getNewsById(Long id) {
        lt.liutikas.reddit.model.core.News news = newsService.listNews(id);
        News openApiNews = newsAssembler.assembleNews(news);

        return new ResponseEntity<>(openApiNews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<lt.liutikas.reddit.openapi.model.NewsPage> listNewsByChannel(List<String> subChannels,
                                                                                       lt.liutikas.reddit.openapi.model.Channel channel,
                                                                                       Integer pageToken,
                                                                                       Integer pageSize) {
        GetNewsRequest request = new GetNewsRequest(subChannels, pageToken, pageSize);
        Channel coreChannel = Channel.valueOf(channel.getValue());

        NewsPage newsPage = newsService.listNews(coreChannel, request);

        lt.liutikas.reddit.openapi.model.NewsPage openApiNewsPage = getOpenApiNewsPage(newsPage);

        return new ResponseEntity<>(openApiNewsPage, HttpStatus.OK);
    }


    private lt.liutikas.reddit.openapi.model.NewsPage getOpenApiNewsPage(NewsPage newsPage) {
        lt.liutikas.reddit.openapi.model.NewsPage openApiNewsPage = new lt.liutikas.reddit.openapi.model.NewsPage();

        List<News> openApiNews = newsPage.getNews().stream()
                .map(newsAssembler::assembleNews)
                .collect(Collectors.toList());

        openApiNewsPage.setNextPageToken(newsPage.getNextPageToken());
        openApiNewsPage.setNews(openApiNews);

        return openApiNewsPage;
    }

}