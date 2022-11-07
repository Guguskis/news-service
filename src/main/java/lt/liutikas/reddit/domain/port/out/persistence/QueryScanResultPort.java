package lt.liutikas.reddit.domain.port.out.persistence;

import lt.liutikas.reddit.domain.entity.scan.ScanResult;

import java.net.URL;
import java.util.Optional;

public interface QueryScanResultPort {

    Optional<ScanResult> findById(URL url);

}
