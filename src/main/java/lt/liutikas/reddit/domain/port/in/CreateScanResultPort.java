package lt.liutikas.reddit.domain.port.in;

import lt.liutikas.reddit.domain.entity.scan.ScanResult;

public interface CreateScanResultPort {

    ScanResult create(ScanResult scanResult);

}
