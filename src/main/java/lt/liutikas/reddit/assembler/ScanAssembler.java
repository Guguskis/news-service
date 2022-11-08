package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.entity.scan.ScanResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScanAssembler {

    public ScanResult assembleScanResult(News news) {
        ScanResult scanResult = new ScanResult();

        scanResult.setUrl(news.getUrl());
        scanResult.setScannedAt(LocalDateTime.now());
        scanResult.setSource(news.getChannel());

        return scanResult;
    }

}
