package lt.liutikas.reddit.assembler;

import com.azure.ai.textanalytics.models.DocumentSentiment;
import lt.liutikas.reddit.model.Sentiment;
import lt.liutikas.reddit.model.SentimentType;
import org.springframework.stereotype.Component;

@Component
public class SentimentAssembler {

    public Sentiment assembleSentiment(DocumentSentiment documentSentiment) {
        Sentiment sentiment = new Sentiment();

        sentiment.setSentiment(SentimentType.valueOf(documentSentiment.getSentiment().toString().toUpperCase()));
        sentiment.setScoreNegative(documentSentiment.getConfidenceScores().getNegative());
        sentiment.setScoreNeutral(documentSentiment.getConfidenceScores().getNeutral());
        sentiment.setScorePositive(documentSentiment.getConfidenceScores().getPositive());

        return sentiment;
    }

}
