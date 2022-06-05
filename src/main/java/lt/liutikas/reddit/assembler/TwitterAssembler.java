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
        tweet.setUrl(getUrl(status)); // todo fix

        return tweet;
    }

    private LocalDateTime getCreatedAt(Status status) {
        return LocalDateTime.ofInstant(status.getCreatedAt().toInstant(), ZoneOffset.UTC);
    }

    private URL getUrl(Status status) {
        URL url;
        try {
            String source = status.getSource();
//            <a href="http://twitter.com/download/android" rel="nofollow">Twitter for Android</a>
            url = new URL(source.substring(source.indexOf("http"), source.indexOf("\" rel")));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }
}
