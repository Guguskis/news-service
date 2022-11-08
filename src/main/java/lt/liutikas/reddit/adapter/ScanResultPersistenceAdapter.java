package lt.liutikas.reddit.adapter;

import lt.liutikas.reddit.domain.entity.scan.ScanResult;
import lt.liutikas.reddit.domain.port.in.persistence.CreateScanResultPort;
import lt.liutikas.reddit.domain.port.out.persistence.QueryScanResultPort;
import lt.liutikas.reddit.repository.ScanResultRepository;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Optional;

@Service
public class ScanResultPersistenceAdapter implements QueryScanResultPort, CreateScanResultPort {

    private final ScanResultRepository scanResultRepository;

    public ScanResultPersistenceAdapter(ScanResultRepository scanResultRepository) {
        this.scanResultRepository = scanResultRepository;
    }

    @Override
    public Optional<ScanResult> findById(URL url) {
        return scanResultRepository.findById(url);
    }


    @Override
    public ScanResult create(ScanResult scanResult) {
        return scanResultRepository.save(scanResult);
    }


}
