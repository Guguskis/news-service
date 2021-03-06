package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.model.core.Channel;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.twitter.Tweet;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import some.developer.reddit.client.model.Submission;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class NewsAssembler {

    private final Clock clock;

    public NewsAssembler(Clock clock) {
        this.clock = clock;
    }

    public News assembleNews(Submission submission) {
        News news = new News();

        news.setTitle(submission.getTitle());
        news.setUrl(submission.getUrl());
        news.setCreated(submission.getCreated());
        news.setChannel(Channel.REDDIT);
        news.setSubChannel(submission.getSubreddit());

        return news;
    }

    public News assembleNews(SaveNewsRequest request) {
        News news = new News();

        news.setTitle(request.getTitle());
        news.setUrl(request.getUrl());
        news.setCreated(LocalDateTime.now(clock));
        news.setChannel(request.getChannel());
        news.setSubChannel(request.getSubChannel());

        return news;
    }


    public News assembleNews(Tweet tweet) {
        News news = new News();

        news.setTitle(tweet.getText());
        news.setUrl(tweet.getUrl());
        news.setCreated(tweet.getCreatedAt());
        news.setChannel(Channel.TWITTER);

        switch (tweet.subChannelType()) {
            case USER:
                news.setSubChannel("@" + tweet.getUser());
                break;
            case KEYWORD:
                news.setSubChannel("#" + tweet.getKeyword());
                break;
            default:
                throw new NotImplementedException("Not implemented sub channel type: " + tweet.subChannelType());
        }

        return news;
    }
}
