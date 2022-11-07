package lt.liutikas.reddit.adapter.in.scheduler;

import lt.liutikas.reddit.domain.usecase.scannews.ScanNewsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScanScheduler {

    private final ScanNewsUseCase scanNewsUseCase;

    public ScanScheduler(ScanNewsUseCase scanNewsUseCase) {
        this.scanNewsUseCase = scanNewsUseCase;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void scan() {
        scanNewsUseCase.scanNews();
    }

}
