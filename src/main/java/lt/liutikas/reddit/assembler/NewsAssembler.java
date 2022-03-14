package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.model.Channel;
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
        news.setSubChannel(event.getSubChannel());

        return news;
    }

    public ScannedNewsEvent assembleScannedNewsEvent(Submission submission) {
        ScannedNewsEvent event = new ScannedNewsEvent(this);

        event.setUrl(submission.getUrl());
        event.setTitle(submission.getTitle());
        event.setCreated(submission.getCreated());
        event.setSubChannel(submission.getSubreddit());
        event.setChannel(Channel.REDDIT);

        return event;
    }
}
