package lt.liutikas.reddit.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "scan")
public class ScanProperties {
    private List<String> subreddits;

    public List<String> getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(List<String> subreddits) {
        this.subreddits = subreddits;
    }
}
