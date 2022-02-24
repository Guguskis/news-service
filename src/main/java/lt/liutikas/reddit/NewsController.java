package lt.liutikas.reddit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @RequestMapping
    public String getNews() {
        return "Hello World!";
    }
}
