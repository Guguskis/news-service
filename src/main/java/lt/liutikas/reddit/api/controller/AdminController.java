package lt.liutikas.reddit.api.controller;

import lt.liutikas.reddit.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface AdminController {
    @GetMapping("/users")
    List<User> getUsers();

    @PostMapping("/news/reddit/scan")
    void scanNews();

    @PostMapping("/news/sentiments/process")
    void processSentiment();
}
