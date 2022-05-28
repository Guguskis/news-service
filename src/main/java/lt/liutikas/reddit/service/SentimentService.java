package lt.liutikas.reddit.service;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.AnalyzeSentimentOptions;
import com.azure.ai.textanalytics.models.AnalyzeSentimentResult;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import lt.liutikas.reddit.assembler.SentimentAssembler;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.ProcessingStatus;
import lt.liutikas.reddit.model.SentimentResult;
import lt.liutikas.reddit.model.event.SavedNewsEvent;
import lt.liutikas.reddit.repository.NewsRepository;
import lt.liutikas.reddit.repository.SentimentResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SentimentService {

    private static final Logger LOG = LoggerFactory.getLogger(SentimentService.class);

    private final TextAnalyticsClient textAnalyticsClient;
    private final NewsRepository newsRepository;
    private final SentimentAssembler sentimentAssembler;
    private final SentimentResultRepository sentimentResultRepository;

    public SentimentService(TextAnalyticsClient textAnalyticsClient, NewsRepository newsRepository, SentimentAssembler sentimentAssembler, SentimentResultRepository sentimentResultRepository) {
        this.textAnalyticsClient = textAnalyticsClient;
        this.newsRepository = newsRepository;
        this.sentimentAssembler = sentimentAssembler;
        this.sentimentResultRepository = sentimentResultRepository;
    }

    @EventListener
    public void handleScannedNewsEvent(SavedNewsEvent event) {
        News news = event.getNews();
        SentimentResult sentimentResult = new SentimentResult();
        sentimentResult.setNews(news);
        sentimentResult.setStatus(ProcessingStatus.NOT_STARTED);

        sentimentResultRepository.save(sentimentResult);
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void processSentiments() {
        List<SentimentResult> results = sentimentResultRepository.findTop5ByStatus(ProcessingStatus.NOT_STARTED);

        if (results.isEmpty()) {
            return;
        }

        List<News> news = results.stream()
                .map(SentimentResult::getNews)
                .collect(Collectors.toList());

        List<String> documents = news.stream()
                .map(News::getTitle)
                .collect(Collectors.toList());

        List<AnalyzeSentimentResult> analysisResults = textAnalyticsClient.analyzeSentimentBatch(documents, "en", new AnalyzeSentimentOptions())
                .stream().collect(Collectors.toList());

        for (int i = 0; i < analysisResults.size(); i++) {
            DocumentSentiment documentSentiment = analysisResults.get(i).getDocumentSentiment();
            News newsItem = news.get(i);
            newsItem.setSentiment(sentimentAssembler.assembleSentiment(documentSentiment));
            newsRepository.save(newsItem);
        }

        results.forEach(result -> result.setStatus(ProcessingStatus.FINISHED));
        sentimentResultRepository.saveAll(results);

        LOG.info("Sentiments processing finished { \"count\": {} }", results.size());
    }

}
