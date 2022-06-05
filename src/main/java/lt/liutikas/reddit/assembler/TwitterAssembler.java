package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.model.twitter.Tweet;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TwitterAssembler {

    public Tweet assembleTweet(Status status) {
        Tweet tweet = new Tweet();

        tweet.setText(status.getText());
        tweet.setCreatedAt(getCreatedAt(status));
        tweet.setUser(status.getUser().getName());
        tweet.setUrl(getUrl(status));

        return tweet;
    }

    private LocalDateTime getCreatedAt(Status status) {
        return LocalDateTime.ofInstant(status.getCreatedAt().toInstant(), ZoneOffset.UTC);
    }

    private URL getUrl(Status status) {
        long tweetId = status.getId();
        String username = status.getUser().getName();
        String url = String.format("https://twitter.com/%s/status/%d", username, tweetId);

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(String.format("This is not URL { \"url\": \"%s\" }", url), e);
        }
    }
}
