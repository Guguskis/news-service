package lt.liutikas.reddit.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

@Configuration
public class WebConfig {

    @Bean
    @Qualifier("reddit")
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri("https://old.reddit.com")
                .build();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}