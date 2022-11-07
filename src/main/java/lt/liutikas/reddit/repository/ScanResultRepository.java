package lt.liutikas.reddit.repository;

import lt.liutikas.reddit.domain.entity.scan.ScanResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.URL;

@Repository
public interface ScanResultRepository extends JpaRepository<ScanResult, URL> {
}
