package lt.liutikas.reddit.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "scan")
public class ScanProperties {

    private List<String> subreddits = new ArrayList<>();
    private List<String> twitterKeywords = new ArrayList<>();
    private List<String> twitterUsers = new ArrayList<>();

    public List<String> getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(List<String> subreddits) {
        this.subreddits = subreddits;
    }

    public List<String> getTwitterKeywords() {
        return twitterKeywords;
    }

    public void setTwitterKeywords(List<String> twitterKeywords) {
        this.twitterKeywords = twitterKeywords;
    }

    public List<String> getTwitterUsers() {
        return twitterUsers;
    }

    public void setTwitterUsers(List<String> twitterUsers) {
        this.twitterUsers = twitterUsers;
    }
}
