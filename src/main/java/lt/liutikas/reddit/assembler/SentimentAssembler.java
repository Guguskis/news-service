package lt.liutikas.reddit.assembler;

import com.azure.ai.textanalytics.models.AnalyzeSentimentResult;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.core.Sentiment;
import lt.liutikas.reddit.model.core.SentimentType;
import org.springframework.stereotype.Component;

@Component
public class SentimentAssembler {

    public Sentiment assembleSentiment(News newsItem, AnalyzeSentimentResult analyzeSentimentResult) {
        DocumentSentiment documentSentiment = analyzeSentimentResult.getDocumentSentiment();

        Sentiment sentiment = new Sentiment();

        sentiment.setSentiment(SentimentType.valueOf(documentSentiment.getSentiment().toString().toUpperCase()));
        sentiment.setScoreNegative(documentSentiment.getConfidenceScores().getNegative());
        sentiment.setScoreNeutral(documentSentiment.getConfidenceScores().getNeutral());
        sentiment.setScorePositive(documentSentiment.getConfidenceScores().getPositive());
        sentiment.setNews(newsItem);

        return sentiment;
    }

}
