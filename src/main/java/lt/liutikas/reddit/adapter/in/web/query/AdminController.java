package lt.liutikas.reddit.adapter.in.web.query;

import lt.liutikas.reddit.domain.entity.core.User;
import lt.liutikas.reddit.domain.usecase.queryactiveusers.QueryActiveUsersUseCase;
import lt.liutikas.reddit.domain.usecase.scannews.ScanNewsUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final QueryActiveUsersUseCase queryActiveUsersUseCase;
    private final ScanNewsUseCase scanNewsUseCase;

    public AdminController(QueryActiveUsersUseCase queryActiveUsersUseCase, ScanNewsUseCase scanNewsUseCase) {
        this.queryActiveUsersUseCase = queryActiveUsersUseCase;
        this.scanNewsUseCase = scanNewsUseCase;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return queryActiveUsersUseCase.listActiveUsers();
    }

    @PostMapping("/news/scan")
    public void scanNews() {
        scanNewsUseCase.scanNews();
    }

}
