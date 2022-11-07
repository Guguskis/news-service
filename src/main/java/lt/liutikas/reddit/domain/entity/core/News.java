package lt.liutikas.reddit.domain.entity.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDateTime;

@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 2048)
    private String title;
    private URL url;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime created;
    private String subChannel;
    @Enumerated(EnumType.STRING)
    private Channel channel;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    @JsonManagedReference
    private Sentiment sentiment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(String subChannel) {
        this.subChannel = subChannel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url=" + url +
                ", created=" + created +
                ", subChannel='" + subChannel + '\'' +
                ", channel=" + channel +
                ", sentiment=" + sentiment +
                '}';
    }
}
