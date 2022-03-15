package lt.liutikas.reddit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class WebConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}