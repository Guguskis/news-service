package lt.liutikas.reddit.domain.entity.core;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Sentiment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(mappedBy = "sentiment")
    @JsonBackReference
    private News news;
    @Enumerated(EnumType.STRING)
    private SentimentType sentiment;
    private double scoreNegative;
    private double scorePositive;
    private double scoreNeutral;

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

    public SentimentType getSentiment() {
        return sentiment;
    }

    public void setSentiment(SentimentType sentiment) {
        this.sentiment = sentiment;
    }

    public double getScoreNegative() {
        return scoreNegative;
    }

    public void setScoreNegative(double scoreNegative) {
        this.scoreNegative = scoreNegative;
    }

    public double getScorePositive() {
        return scorePositive;
    }

    public void setScorePositive(double scorePositive) {
        this.scorePositive = scorePositive;
    }

    public double getScoreNeutral() {
        return scoreNeutral;
    }

    public void setScoreNeutral(double scoreNeutral) {
        this.scoreNeutral = scoreNeutral;
    }
}
