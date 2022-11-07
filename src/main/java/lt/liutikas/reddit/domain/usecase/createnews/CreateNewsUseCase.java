package lt.liutikas.reddit.domain.usecase.createnews;

import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.domain.port.out.persistence.CreateNewsPort;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.event.NewsPublisher;
import lt.liutikas.reddit.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CreateNewsUseCase implements CreateNewsPort {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewsUseCase.class);

    private final NewsAssembler newsAssembler;
    private final NewsRepository newsRepository;

    private final NewsPublisher newsPublisher;

    public CreateNewsUseCase(NewsRepository newsRepository, NewsAssembler newsAssembler, NewsPublisher newsPublisher) {
        this.newsRepository = newsRepository;
        this.newsAssembler = newsAssembler;
        this.newsPublisher = newsPublisher;
    }

    @Override
    public News create(SaveNewsRequest request) {
        News news = newsAssembler.assembleNews(request);
        news = newsRepository.save(news);

        LOG.info("Saved news { \"id\": {} }", news.getId());

        newsPublisher.publishNews(Arrays.asList(news));

        return news;
    }

    @Override
    public News create(News news) {
        return newsRepository.save(news);
    }

}
