package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.domain.entity.core.Channel;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.entity.scan.ScanResult;
import org.springframework.stereotype.Component;
import some.developer.reddit.client.model.Submission;

import java.time.LocalDateTime;

@Component
public class ScanAssembler {

    public ScanResult assembleScanResult(Submission submission) {
        ScanResult scanResult = new ScanResult();

        scanResult.setUrl(submission.getUrl());
        scanResult.setScannedAt(LocalDateTime.now());
        scanResult.setSource(Channel.REDDIT);

        return scanResult;
    }

    public ScanResult assembleScanResult(News news) {
        ScanResult scanResult = new ScanResult();

        scanResult.setUrl(news.getUrl());
        scanResult.setScannedAt(LocalDateTime.now());
        scanResult.setSource(news.getChannel());

        return scanResult;
    }

}
