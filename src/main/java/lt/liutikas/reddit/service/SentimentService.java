package lt.liutikas.reddit.service;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.AnalyzeSentimentOptions;
import com.azure.ai.textanalytics.models.AnalyzeSentimentResult;
import com.azure.ai.textanalytics.models.SentenceSentiment;
import lt.liutikas.reddit.assembler.SentimentAssembler;
import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.ProcessingStatus;
import lt.liutikas.reddit.model.Sentiment;
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

    public SentimentService(TextAnalyticsClient textAnalyticsClient, SentimentAssembler sentimentAssembler, SentimentResultRepository sentimentResultRepository, NewsRepository newsRepository) {
        this.textAnalyticsClient = textAnalyticsClient;
        this.sentimentAssembler = sentimentAssembler;
        this.sentimentResultRepository = sentimentResultRepository;
        this.newsRepository = newsRepository;
    }

    @EventListener
    public void handleScannedNewsEvent(SavedNewsEvent event) {
        News news = event.getNews();
        SentimentResult sentimentResult = new SentimentResult();
        sentimentResult.setNews(news);
        sentimentResult.setStatus(ProcessingStatus.NOT_STARTED);

        sentimentResultRepository.save(sentimentResult);
    }

    @Scheduled(cron = "10 0/1 * * * *")
    public void processSentiments() {
        List<SentimentResult> results = sentimentResultRepository.findTop5ByStatus(ProcessingStatus.NOT_STARTED);

        if (results.isEmpty()) {
            return;
        }

        for (SentimentResult result : enrichWithSentimentAnalysis(results)) {
            result.setStatus(ProcessingStatus.FINISHED);
            sentimentResultRepository.save(result);
            newsRepository.save(result.getNews());
        }

        LOG.info("Sentiments processing finished { \"count\": {} }", results.size());
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
