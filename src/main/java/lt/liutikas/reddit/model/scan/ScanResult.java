package lt.liutikas.reddit.model.scan;

import lt.liutikas.reddit.model.core.Channel;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.net.URL;
import java.time.LocalDateTime;

@Entity
public class ScanResult {

    @Id
    private URL url;
    private LocalDateTime scannedAt;
    @Enumerated(EnumType.STRING)
    private Channel source;

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

    public Channel getSource() {
        return source;
    }

    public void setSource(Channel source) {
        this.source = source;
    }
}
