package lt.liutikas.reddit.opm;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpmWebConfig {

    @Value("${discord.token}")
    private String discordToken;

    @Bean(name = "opm")
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri("https://www.read-one-punchman.com/")
                .build();
    }

    @Bean
    public DiscordApi discordApi() {
        return new DiscordApiBuilder()
                .setToken(discordToken)
                .login()
                .join();
    }
}
