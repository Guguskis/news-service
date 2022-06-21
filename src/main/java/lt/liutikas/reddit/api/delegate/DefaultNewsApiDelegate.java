package lt.liutikas.reddit.api.delegate;

import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.openapi.api.NewsApiDelegate;
import lt.liutikas.reddit.openapi.model.ListNews200Response;
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
    public ResponseEntity<ListNews200Response> listNews(List<String> subChannels, Integer pageToken, Integer pageSize) {
        GetNewsRequest request = new GetNewsRequest(subChannels, pageToken, pageSize);

        NewsPage newsPage = newsService.getNews(request);

        ListNews200Response response = new ListNews200Response();

        response.setNews(assembleNews(newsPage.getNews()));
        response.setNextPageToken(newsPage.getNextPageToken());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<News> assembleNews(List<lt.liutikas.reddit.model.core.News> news) {
        return news.stream()
                .map(newsAssembler::assembleNews)
                .collect(Collectors.toList());
    }

}
