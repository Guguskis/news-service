package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.model.core.Channel;
import lt.liutikas.reddit.model.core.News;
import lt.liutikas.reddit.model.twitter.Tweet;
import lt.liutikas.reddit.openapi.model.CreateNewsRequest;
import lt.liutikas.reddit.openapi.model.Sentiment;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import some.developer.reddit.client.model.Submission;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Component
public class NewsAssembler {

    private final Clock clock;

    public NewsAssembler(Clock clock) {
        this.clock = clock;
    }

    public lt.liutikas.reddit.openapi.model.News assembleNews(News news) {
        lt.liutikas.reddit.openapi.model.News openApiNews = new lt.liutikas.reddit.openapi.model.News();

        openApiNews.setId(news.getId());
        openApiNews.setTitle(news.getTitle());
        openApiNews.setSubChannel(news.getSubChannel());
        openApiNews.setUrl(news.getUrl().toString());
        openApiNews.setCreated(OffsetDateTime.of(news.getCreated(), ZoneOffset.UTC));
        openApiNews.setChannel(lt.liutikas.reddit.openapi.model.Channel.fromValue(news.getChannel().name()));
        if (news.getSentiment() != null) {
            openApiNews.setSentiment(assembleSentiment(news.getSentiment()));
        }

        return openApiNews;
    }

    private Sentiment assembleSentiment(lt.liutikas.reddit.model.core.Sentiment sentiment) {
        Sentiment openApiSentiment = new Sentiment();

        openApiSentiment.setId(sentiment.getId());
        openApiSentiment.setSentiment(lt.liutikas.reddit.openapi.model.SentimentType.valueOf(sentiment.getSentiment().name()));
        openApiSentiment.setScorePositive(sentiment.getScorePositive());
        openApiSentiment.setScoreNegative(sentiment.getScoreNegative());
        openApiSentiment.setScoreNeutral(sentiment.getScoreNeutral());

        return openApiSentiment;
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

    public lt.liutikas.reddit.openapi.model.NewsPage assembleNewsPage(Page<News> page) {
        lt.liutikas.reddit.openapi.model.NewsPage newsPage = new lt.liutikas.reddit.openapi.model.NewsPage();
        newsPage.setNews(page.getContent().stream().map(this::assembleNews).collect(Collectors.toList()));
        if (page.nextPageable().isPaged())
            newsPage.setNextPageToken(page.nextPageable().getPageNumber());
        return newsPage;
    }

    public News assembleNews(CreateNewsRequest request) {
        News news = new News();

        news.setSubChannel(request.getSubChannel());
        news.setTitle(request.getTitle());
        news.setCreated(LocalDateTime.now(clock));
        news.setChannel(Channel.valueOf(request.getChannel().getValue()));
        news.setUrl(parseUrl(request));

        return news;
    }

    private URL parseUrl(CreateNewsRequest createNewsRequest) {
        try {
            return new URL(createNewsRequest.getUrl());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(String.format("\"%s\" is not a valid url", createNewsRequest.getUrl()), e);
        }
    }
}
