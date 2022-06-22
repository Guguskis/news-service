package lt.liutikas.reddit.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import lt.liutikas.reddit.openapi.model.News;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * NewsPage
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T18:52:21.870437300+03:00[Europe/Vilnius]")
public class NewsPage {

  @JsonProperty("nextPageToken")
  private Integer nextPageToken;

  @JsonProperty("news")
  @Valid
  private List<News> news = null;

  public NewsPage nextPageToken(Integer nextPageToken) {
    this.nextPageToken = nextPageToken;
    return this;
  }

  /**
   * Get nextPageToken
   * @return nextPageToken
  */
  
  @Schema(name = "nextPageToken", required = false)
  public Integer getNextPageToken() {
    return nextPageToken;
  }

  public void setNextPageToken(Integer nextPageToken) {
    this.nextPageToken = nextPageToken;
  }

  public NewsPage news(List<News> news) {
    this.news = news;
    return this;
  }

  public NewsPage addNewsItem(News newsItem) {
    if (this.news == null) {
      this.news = new ArrayList<>();
    }
    this.news.add(newsItem);
    return this;
  }

  /**
   * Get news
   * @return news
  */
  @Valid 
  @Schema(name = "news", required = false)
  public List<News> getNews() {
    return news;
  }

  public void setNews(List<News> news) {
    this.news = news;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewsPage newsPage = (NewsPage) o;
    return Objects.equals(this.nextPageToken, newsPage.nextPageToken) &&
        Objects.equals(this.news, newsPage.news);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nextPageToken, news);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewsPage {\n");
    sb.append("    nextPageToken: ").append(toIndentedString(nextPageToken)).append("\n");
    sb.append("    news: ").append(toIndentedString(news)).append("\n");
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

