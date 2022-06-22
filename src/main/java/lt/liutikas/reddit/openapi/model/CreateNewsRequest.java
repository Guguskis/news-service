package lt.liutikas.reddit.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import lt.liutikas.reddit.openapi.model.Channel;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * CreateNewsRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T18:48:18.912558600+03:00[Europe/Vilnius]")
public class CreateNewsRequest {

  @JsonProperty("title")
  private String title;

  @JsonProperty("url")
  private String url;

  @JsonProperty("channel")
  private Channel channel;

  @JsonProperty("subChannel")
  private String subChannel;

  public CreateNewsRequest title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
  */
  
  @Schema(name = "title", required = false)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public CreateNewsRequest url(String url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
  */
  
  @Schema(name = "url", required = false)
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public CreateNewsRequest channel(Channel channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
  */
  @Valid 
  @Schema(name = "channel", required = false)
  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public CreateNewsRequest subChannel(String subChannel) {
    this.subChannel = subChannel;
    return this;
  }

  /**
   * Get subChannel
   * @return subChannel
  */
  
  @Schema(name = "subChannel", required = false)
  public String getSubChannel() {
    return subChannel;
  }

  public void setSubChannel(String subChannel) {
    this.subChannel = subChannel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateNewsRequest createNewsRequest = (CreateNewsRequest) o;
    return Objects.equals(this.title, createNewsRequest.title) &&
        Objects.equals(this.url, createNewsRequest.url) &&
        Objects.equals(this.channel, createNewsRequest.channel) &&
        Objects.equals(this.subChannel, createNewsRequest.subChannel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, url, channel, subChannel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateNewsRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    subChannel: ").append(toIndentedString(subChannel)).append("\n");
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

