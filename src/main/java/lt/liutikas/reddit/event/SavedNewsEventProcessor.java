package lt.liutikas.reddit.event;

import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.ProcessingStatus;
import lt.liutikas.reddit.model.SentimentResult;
import lt.liutikas.reddit.model.event.SavedNewsEvent;
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

    public SavedNewsEventProcessor(SentimentResultRepository sentimentResultRepository) {
        this.sentimentResultRepository = sentimentResultRepository;
    }

    @EventListener
    public void queueSentimentUpdate(SavedNewsEvent event) {
        List<SentimentResult> sentimentResult = event.getNews().stream()
                .map(this::assembleNotStartedSentimentResult)
                .collect(Collectors.toList());

        sentimentResultRepository.saveAll(sentimentResult);

        LOG.info("Queued sentiment update for news { \"count\": {} }", sentimentResult.size());
    }

    private SentimentResult assembleNotStartedSentimentResult(News news) {
        SentimentResult sentimentResult = new SentimentResult();
        sentimentResult.setNews(news);
        sentimentResult.setStatus(ProcessingStatus.NOT_STARTED);
        return sentimentResult;
    }

}
