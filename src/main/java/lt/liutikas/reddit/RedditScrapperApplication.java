package lt.liutikas.reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"lt.liutikas", "some.developer"})
public class RedditScrapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedditScrapperApplication.class, args);
    }

}
