package lt.liutikas.reddit.config;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import lt.liutikas.reddit.config.properties.AzureProperties;
import lt.liutikas.reddit.config.properties.TwitterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class ExternalServicesConfig {

    @Bean
    public TextAnalyticsClient textAnalyticsClient(AzureProperties properties) {
        return new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(properties.getKey()))
                .endpoint(properties.getEndpoint())
                .buildClient();
    }

    @Bean
    public Twitter twitter(TwitterProperties properties) {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(properties.getConsumerKey())
                .setOAuthConsumerSecret(properties.getConsumerSecret())
                .setOAuthAccessToken(properties.getAccessToken())
                .setOAuthAccessTokenSecret(properties.getAccessTokenSecret());
        TwitterFactory tf = new TwitterFactory(cb.build());

        return tf.getInstance();
    }

}
