package lt.liutikas.reddit.domain.port.in.persistence;

import lt.liutikas.reddit.domain.entity.scan.ScanResult;

public interface CreateScanResultPort {

    ScanResult create(ScanResult scanResult);

}
