package lt.liutikas.reddit.service;

import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.config.exception.NotFoundException;
import lt.liutikas.reddit.event.EventPublisher;
import lt.liutikas.reddit.model.core.Channel;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsService.class);

    private final NewsAssembler newsAssembler;
    private final NewsRepository newsRepository;
    private final EventPublisher eventPublisher;

    public NewsService(NewsRepository newsRepository, EventPublisher eventPublisher, NewsAssembler newsAssembler) {
        this.newsRepository = newsRepository;
        this.eventPublisher = eventPublisher;
        this.newsAssembler = newsAssembler;
    }

    // todo try @SubscribeMapping
    // https://stackoverflow.com/questions/24890450/spring-stomp-subscribemapping-for-user-destination

    public News getNews(Long id) {
        Optional<News> optional = newsRepository.findById(id);

        if (optional.isEmpty())
            throw new NotFoundException(String.format("News not found { \"id\": %s }", id));

        return optional.get();
    }

    public NewsPage getNews(GetNewsRequest request) {
        PageRequest pageRequest = getPageRequestByCreatedDesc(request);
        List<String> subChannels = request.getSubChannels();

        Page<News> page = findNews(pageRequest, subChannels);

        NewsPage newsPage = new NewsPage();
        newsPage.setNews(page.getContent());
        newsPage.setNextToken(page);

        LOG.info("Returning news { 'pageToken': {}, 'pageSize': {} }", pageRequest.getPageNumber(), request.getPageSize());

        return newsPage;
    }

    public NewsPage getNews(Channel channel, GetNewsRequest request) {
        PageRequest pageRequest = getPageRequestByCreatedDesc(request);
        List<String> subChannels = request.getSubChannels();
        Page<News> page = findNews(channel, subChannels, pageRequest);

        NewsPage newsPage = new NewsPage();
        newsPage.setNews(page.getContent());
        newsPage.setNextToken(page);

        LOG.info("Returning news { 'channel': '{}''pageToken': {}, 'pageSize': {} }", channel, pageRequest.getPageNumber(), request.getPageSize());

        return newsPage;
    }

    private Page<News> findNews(PageRequest pageRequest, List<String> subChannels) {
        if (subChannels.isEmpty()) {
            return newsRepository.findAll(pageRequest);
        } else {
            return newsRepository.findBySubChannelInIgnoreCase(subChannels, pageRequest);
        }
    }

    private Page<News> findNews(Channel channel, List<String> subChannels, PageRequest pageRequest) {
        if (subChannels.isEmpty()) {
            return newsRepository.findByChannel(channel, pageRequest);
        } else {
            return newsRepository.findByChannelAndSubChannelInIgnoreCase(channel, subChannels, pageRequest);
        }
    }

    private PageRequest getPageRequestByCreatedDesc(GetNewsRequest request) {
        PageRequest pageRequest = request.pageRequest();
        Sort sort = Sort.by("created").descending();
        return pageRequest.withSort(sort);
    }

    public News saveNews(SaveNewsRequest request) {
        News news = newsAssembler.assembleNews(request);
        news = newsRepository.save(news);

        LOG.info("Saved news { \"id\": {} }", news.getId());

        eventPublisher.publishSavedNewsEvent(Arrays.asList(news));

        return news;
    }
}
