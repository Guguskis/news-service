package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.event.ScannedNewsEvent;
import lt.liutikas.reddit.model.reddit.Submission;
import org.springframework.stereotype.Component;

@Component
public class NewsAssembler {

    public News assembleNews(ScannedNewsEvent event) {
        News news = new News();

        news.setTitle(event.getTitle());
        news.setUrl(event.getUrl());
        news.setCreated(event.getCreated());
        news.setChannel(event.getChannel());

        return news;
    }

    public ScannedNewsEvent assembleScannedNewsEvent(Submission submission) {
        ScannedNewsEvent scannedNewsEvent = new ScannedNewsEvent(this);

        scannedNewsEvent.setUrl(submission.getUrl());
        scannedNewsEvent.setTitle(submission.getTitle());
        scannedNewsEvent.setCreated(submission.getCreated());
        scannedNewsEvent.setChannel(submission.getSubreddit());

        return scannedNewsEvent;
    }
}
