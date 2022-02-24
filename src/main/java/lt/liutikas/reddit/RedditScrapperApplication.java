package lt.liutikas.reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RedditScrapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedditScrapperApplication.class, args);
    }

}
