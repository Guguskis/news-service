package lt.liutikas.reddit.openapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import java.util.Objects;

/**
 * NewsSentiment
 */

@JsonTypeName("News_sentiment")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T01:07:54.033581300+03:00[Europe/Vilnius]")
public class NewsSentiment {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("sentiment")
  private String sentiment;

  @JsonProperty("scoreNegative")
  private Double scoreNegative;

  @JsonProperty("scorePositive")
  private Double scorePositive;

  @JsonProperty("scoreNeutral")
  private Double scoreNeutral;

  public NewsSentiment id(Long id) {
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

  public NewsSentiment sentiment(String sentiment) {
    this.sentiment = sentiment;
    return this;
  }

  /**
   * Get sentiment
   *
   * @return sentiment
   */

  @Schema(name = "sentiment", required = false)
  public String getSentiment() {
    return sentiment;
  }

  public void setSentiment(String sentiment) {
    this.sentiment = sentiment;
  }

  public NewsSentiment scoreNegative(Double scoreNegative) {
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

  public NewsSentiment scorePositive(Double scorePositive) {
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

  public NewsSentiment scoreNeutral(Double scoreNeutral) {
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
    NewsSentiment newsSentiment = (NewsSentiment) o;
    return Objects.equals(this.id, newsSentiment.id) &&
            Objects.equals(this.sentiment, newsSentiment.sentiment) &&
            Objects.equals(this.scoreNegative, newsSentiment.scoreNegative) &&
            Objects.equals(this.scorePositive, newsSentiment.scorePositive) &&
            Objects.equals(this.scoreNeutral, newsSentiment.scoreNeutral);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sentiment, scoreNegative, scorePositive, scoreNeutral);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewsSentiment {\n");
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
}
