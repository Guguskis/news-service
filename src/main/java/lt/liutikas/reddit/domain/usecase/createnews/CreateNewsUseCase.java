package lt.liutikas.reddit.domain.usecase.createnews;

import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.assembler.NewsAssembler;
import lt.liutikas.reddit.domain.port.out.persistence.CreateNewsPort;
import lt.liutikas.reddit.event.EventPublisher;
import lt.liutikas.reddit.domain.entity.core.News;
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
    private final EventPublisher eventPublisher;

    public CreateNewsUseCase(NewsRepository newsRepository, EventPublisher eventPublisher, NewsAssembler newsAssembler) {
        this.newsRepository = newsRepository;
        this.eventPublisher = eventPublisher;
        this.newsAssembler = newsAssembler;
    }

    @Override
    public News createNews(SaveNewsRequest request) {
        News news = newsAssembler.assembleNews(request);
        news = newsRepository.save(news);

        LOG.info("Saved news { \"id\": {} }", news.getId());

        // TODO actual persistence, not spring based event something
        eventPublisher.publishSavedNewsEvent(Arrays.asList(news));

        return news;
    }

}
