package lt.liutikas.reddit.api.controller;

import lt.liutikas.reddit.model.core.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/admin")
public interface AdminController {
    @GetMapping("/users")
    List<User> getUsers();

    @PostMapping("/news/reddit/scan")
    void scanNews();

    @PostMapping("/news/sentiments/process")
    void processSentiment();
}
