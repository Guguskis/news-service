package lt.liutikas.reddit.api.delegate;

import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.model.core.Channel;
import lt.liutikas.reddit.openapi.api.NewsApiDelegate;
import lt.liutikas.reddit.openapi.model.CreateNewsRequest;
import lt.liutikas.reddit.openapi.model.News;
import lt.liutikas.reddit.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
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
        NewsPage newsPage = newsService.getNews(request);

        lt.liutikas.reddit.openapi.model.NewsPage openApiNewsPage = new lt.liutikas.reddit.openapi.model.NewsPage();
        List<News> openApiNews = newsPage.getNews().stream()
                .map(newsAssembler::assembleNews)
                .collect(Collectors.toList());

        openApiNewsPage.setNextPageToken(newsPage.getNextPageToken());
        openApiNewsPage.setNews(openApiNews);

        return new ResponseEntity<>(openApiNewsPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<News> createNews(CreateNewsRequest createNewsRequest) {
        SaveNewsRequest request = new SaveNewsRequest();
        request.setSubChannel(createNewsRequest.getSubChannel());
        request.setTitle(createNewsRequest.getTitle());
        request.setChannel(Channel.valueOf(createNewsRequest.getChannel().getValue()));
        request.setUrl(parseUrl(createNewsRequest));

        lt.liutikas.reddit.model.core.News news = newsService.saveNews(request);
        News openApiNews = newsAssembler.assembleNews(news);

        return new ResponseEntity<>(openApiNews, HttpStatus.CREATED);
    }

    private URL parseUrl(CreateNewsRequest createNewsRequest) {
        try {
            return new URL(createNewsRequest.getUrl());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(String.format("\"%s\" is not a valid url", createNewsRequest.getUrl()), e);
        }
    }
}
