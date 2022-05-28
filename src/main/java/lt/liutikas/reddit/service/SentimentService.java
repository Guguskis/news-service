package lt.liutikas.reddit.service;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import lt.liutikas.reddit.model.event.SavedNewsEvent;
import lt.liutikas.reddit.repository.NewsRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SentimentService {

    private final TextAnalyticsClient textAnalyticsClient;
    private final NewsRepository newsRepository;

    public SentimentService(TextAnalyticsClient textAnalyticsClient, NewsRepository newsRepository) {
        this.textAnalyticsClient = textAnalyticsClient;
        this.newsRepository = newsRepository;
    }

    public DocumentSentiment getSentiment(String text) {
        return textAnalyticsClient.analyzeSentiment(text);
    }

    @EventListener
    public void handleScannedNewsEvent(SavedNewsEvent event) {

    }

}
