package lt.liutikas.reddit.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URL;
import java.time.LocalDateTime;

@Entity
public class ScanResult {

    @Id
    private URL url;
    private LocalDateTime scannedAt;
    private ScanSource source;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public LocalDateTime getScannedAt() {
        return scannedAt;
    }

    public void setScannedAt(LocalDateTime scannedAt) {
        this.scannedAt = scannedAt;
    }

    public ScanSource getSource() {
        return source;
    }

    public void setSource(ScanSource source) {
        this.source = source;
    }
}
