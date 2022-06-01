package lt.liutikas.reddit.service;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.AnalyzeSentimentOptions;
import com.azure.ai.textanalytics.models.AnalyzeSentimentResult;
import com.azure.ai.textanalytics.models.SentenceSentiment;
import lt.liutikas.reddit.assembler.SentimentAssembler;
import lt.liutikas.reddit.config.properties.SentimentProperties;
import lt.liutikas.reddit.event.EventPublisher;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.core.Sentiment;
import lt.liutikas.reddit.model.scan.ProcessingStatus;
import lt.liutikas.reddit.model.scan.SentimentResult;
import lt.liutikas.reddit.repository.NewsRepository;
import lt.liutikas.reddit.repository.SentimentResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SentimentService {

    private static final Logger LOG = LoggerFactory.getLogger(SentimentService.class);

    private static final String RESPONSE_MAPPING_ERROR_MESSAGE = "Sentiment response mapping failed, could not find analysed news by text { \"text\": \"{}\", \"{}\"}";

    private final TextAnalyticsClient textAnalyticsClient;
    private final SentimentAssembler sentimentAssembler;
    private final SentimentResultRepository sentimentResultRepository;
    private final NewsRepository newsRepository;
    private final EventPublisher eventPublisher;
    private final SentimentProperties sentimentProperties;

    public SentimentService(TextAnalyticsClient textAnalyticsClient, SentimentAssembler sentimentAssembler, SentimentResultRepository sentimentResultRepository, NewsRepository newsRepository, EventPublisher eventPublisher, SentimentProperties sentimentProperties) {
        this.textAnalyticsClient = textAnalyticsClient;
        this.sentimentAssembler = sentimentAssembler;
        this.sentimentResultRepository = sentimentResultRepository;
        this.newsRepository = newsRepository;
        this.eventPublisher = eventPublisher;
        this.sentimentProperties = sentimentProperties;
    }


    @Scheduled(cron = "10 0/1 * * * *")
    public void processSentiments() {
        if (!sentimentProperties.isEnabled())
            return;

        List<SentimentResult> results = sentimentResultRepository.findTop5ByStatus(ProcessingStatus.NOT_STARTED);

        if (results.isEmpty())
            return;

        List<News> savedNews = new ArrayList<>();
        List<SentimentResult> sentimentResults = enrichWithSentimentAnalysis(results);
        List<News> news = sentimentResults.stream()
                .map(SentimentResult::getNews)
                .collect(Collectors.toList());

        for (SentimentResult result : sentimentResults) {
            SentimentResult finished = savedAsFinished(result);
            savedNews.add(finished.getNews());
        }

        LOG.info("Sentiments processing finished { \"count\": {} }", results.size());

        if (!news.isEmpty())
            eventPublisher.publishUpdatedNewsEvent(savedNews);
    }

    private SentimentResult savedAsFinished(SentimentResult result) {
        result.setStatus(ProcessingStatus.FINISHED);
        sentimentResultRepository.save(result);
        result.setNews(newsRepository.save(result.getNews()));
        return result;
    }

    private List<SentimentResult> enrichWithSentimentAnalysis(List<SentimentResult> results) {
        List<News> news = results.stream()
                .map(SentimentResult::getNews)
                .collect(Collectors.toList());

        List<AnalyzeSentimentResult> sentimentResults = analyseSentiments(news);

        for (AnalyzeSentimentResult result : sentimentResults) {
            String text = getText(result);
            Optional<SentimentResult> optional = results.stream()
                    .filter(r -> r.getNews().getTitle().equals(text))
                    .findFirst();

            if (optional.isEmpty()) {
                LOG.error(RESPONSE_MAPPING_ERROR_MESSAGE, text, news);
                continue;
            }

            News newsItem = optional.get().getNews();
            Sentiment sentiment = sentimentAssembler.assembleSentiment(newsItem, result);
            newsItem.setSentiment(sentiment);

        }

        return results;
    }

    private List<AnalyzeSentimentResult> analyseSentiments(List<News> news) {
        return textAnalyticsClient.analyzeSentimentBatch(getTitles(news), "en", new AnalyzeSentimentOptions())
                .stream().collect(Collectors.toList());
    }

    private String getText(AnalyzeSentimentResult result) {
        return result.getDocumentSentiment()
                .getSentences().stream()
                .map(SentenceSentiment::getText)
                .collect(Collectors.joining(""));
    }

    private List<String> getTitles(List<News> news) {
        return news.stream()
                .map(News::getTitle)
                .collect(Collectors.toList());
    }
}
