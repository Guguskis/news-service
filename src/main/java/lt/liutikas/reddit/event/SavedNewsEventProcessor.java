package lt.liutikas.reddit.event;

import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.event.SavedNewsEvent;
import lt.liutikas.reddit.model.scan.ProcessingStatus;
import lt.liutikas.reddit.model.scan.SentimentResult;
import lt.liutikas.reddit.repository.SentimentResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SavedNewsEventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SavedNewsEventProcessor.class);

    private final SentimentResultRepository sentimentResultRepository;
    private final NewsPublisher newsPublisher;

    public SavedNewsEventProcessor(SentimentResultRepository sentimentResultRepository, NewsPublisher newsPublisher) {
        this.sentimentResultRepository = sentimentResultRepository;
        this.newsPublisher = newsPublisher;
    }

    @EventListener
    public void queueSentimentUpdate(SavedNewsEvent event) {
        List<SentimentResult> sentimentResults = event.getNews().stream()
                .map(this::assembleNotStartedSentimentResult)
                .collect(Collectors.toList());

        sentimentResultRepository.saveAll(sentimentResults);

        LOG.info("Queued sentiment update for news { \"count\": {} }", sentimentResults.size());
    }

    @EventListener
    public void notifySubscribers(SavedNewsEvent event) {
        List<News> news = event.getNews();

        newsPublisher.publishNews(news);

        LOG.info("Notified subscribers about saved news { \"count\": {} }", news.size());
    }

    private SentimentResult assembleNotStartedSentimentResult(News news) {
        SentimentResult sentimentResult = new SentimentResult();
        sentimentResult.setNews(news);
        sentimentResult.setStatus(ProcessingStatus.NOT_STARTED);
        return sentimentResult;
    }

}
