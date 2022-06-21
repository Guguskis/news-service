package lt.liutikas.reddit.openapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ListNews200Response
 */

@JsonTypeName("listNews_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T00:47:59.053065100+03:00[Europe/Vilnius]")
public class ListNews200Response {

  @JsonProperty("nextPageToken")
  private Integer nextPageToken;

  @JsonProperty("news")
  @Valid
  private List<News> news = null;

  public ListNews200Response nextPageToken(Integer nextPageToken) {
    this.nextPageToken = nextPageToken;
    return this;
  }

  /**
   * Get nextPageToken
   *
   * @return nextPageToken
   */

  @Schema(name = "nextPageToken", required = false)
  public Integer getNextPageToken() {
    return nextPageToken;
  }

  public void setNextPageToken(Integer nextPageToken) {
    this.nextPageToken = nextPageToken;
  }

  public ListNews200Response news(List<News> news) {
    this.news = news;
    return this;
  }

  public ListNews200Response addNewsItem(News newsItem) {
    if (this.news == null) {
      this.news = new ArrayList<>();
    }
    this.news.add(newsItem);
    return this;
  }

  /**
   * Get news
   *
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
    ListNews200Response listNews200Response = (ListNews200Response) o;
    return Objects.equals(this.nextPageToken, listNews200Response.nextPageToken) &&
            Objects.equals(this.news, listNews200Response.news);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nextPageToken, news);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListNews200Response {\n");
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

