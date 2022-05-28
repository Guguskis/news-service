package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.News;
import org.springframework.stereotype.Component;
import some.developer.reddit.client.model.Submission;

@Component
public class NewsAssembler {


    public News assembleNews(Submission submission) {
        News news = new News();

        news.setTitle(submission.getTitle());
        news.setUrl(submission.getUrl());
        news.setCreated(submission.getCreated());
        news.setChannel(Channel.REDDIT);
        news.setSubChannel(submission.getSubreddit());

        return news;
    }

}
