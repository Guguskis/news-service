package lt.liutikas.reddit.model;

import javax.persistence.*;

@Entity
public class SentimentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private News news;
    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public ProcessingStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessingStatus status) {
        this.status = status;
    }
}
