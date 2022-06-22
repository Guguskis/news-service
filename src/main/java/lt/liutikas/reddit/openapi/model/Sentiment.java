package lt.liutikas.reddit.openapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import java.util.Objects;

/**
 * Sentiment
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T09:05:48.683327200+03:00[Europe/Vilnius]")
public class Sentiment {

  @JsonProperty("id")
  private Long id;
  @JsonProperty("sentiment")
  private SentimentEnum sentiment;
  @JsonProperty("scoreNegative")
  private Double scoreNegative;
  @JsonProperty("scorePositive")
  private Double scorePositive;
  @JsonProperty("scoreNeutral")
  private Double scoreNeutral;

  public Sentiment id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   *
   * @return id
   */

  @Schema(name = "id", required = false)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Sentiment sentiment(SentimentEnum sentiment) {
    this.sentiment = sentiment;
    return this;
  }

  /**
   * Get sentiment
   *
   * @return sentiment
   */

  @Schema(name = "sentiment", required = false)
  public SentimentEnum getSentiment() {
    return sentiment;
  }

  public void setSentiment(SentimentEnum sentiment) {
    this.sentiment = sentiment;
  }

  public Sentiment scoreNegative(Double scoreNegative) {
    this.scoreNegative = scoreNegative;
    return this;
  }

  /**
   * Get scoreNegative
   *
   * @return scoreNegative
   */

  @Schema(name = "scoreNegative", required = false)
  public Double getScoreNegative() {
    return scoreNegative;
  }

  public void setScoreNegative(Double scoreNegative) {
    this.scoreNegative = scoreNegative;
  }

  public Sentiment scorePositive(Double scorePositive) {
    this.scorePositive = scorePositive;
    return this;
  }

  /**
   * Get scorePositive
   *
   * @return scorePositive
   */

  @Schema(name = "scorePositive", required = false)
  public Double getScorePositive() {
    return scorePositive;
  }

  public void setScorePositive(Double scorePositive) {
    this.scorePositive = scorePositive;
  }

  public Sentiment scoreNeutral(Double scoreNeutral) {
    this.scoreNeutral = scoreNeutral;
    return this;
  }

  /**
   * Get scoreNeutral
   *
   * @return scoreNeutral
   */

  @Schema(name = "scoreNeutral", required = false)
  public Double getScoreNeutral() {
    return scoreNeutral;
  }

  public void setScoreNeutral(Double scoreNeutral) {
    this.scoreNeutral = scoreNeutral;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sentiment sentiment = (Sentiment) o;
    return Objects.equals(this.id, sentiment.id) &&
            Objects.equals(this.sentiment, sentiment.sentiment) &&
            Objects.equals(this.scoreNegative, sentiment.scoreNegative) &&
            Objects.equals(this.scorePositive, sentiment.scorePositive) &&
            Objects.equals(this.scoreNeutral, sentiment.scoreNeutral);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sentiment, scoreNegative, scorePositive, scoreNeutral);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Sentiment {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    sentiment: ").append(toIndentedString(sentiment)).append("\n");
    sb.append("    scoreNegative: ").append(toIndentedString(scoreNegative)).append("\n");
    sb.append("    scorePositive: ").append(toIndentedString(scorePositive)).append("\n");
    sb.append("    scoreNeutral: ").append(toIndentedString(scoreNeutral)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  /**
   * Gets or Sets sentiment
   */
  public enum SentimentEnum {
    POSITIVE("POSITIVE"),

    NEGATIVE("NEGATIVE"),

    NEUTRAL("NEUTRAL"),

    MIXED("MIXED");

    private String value;

    SentimentEnum(String value) {
      this.value = value;
    }

    @JsonCreator
    public static SentimentEnum fromValue(String value) {
      for (SentimentEnum b : SentimentEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }
}

