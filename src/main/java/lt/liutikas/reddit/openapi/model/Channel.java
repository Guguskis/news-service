package lt.liutikas.reddit.openapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Generated;

/**
 * Gets or Sets Channel
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T09:28:58.061353200+03:00[Europe/Vilnius]")
public enum Channel {

  REDDIT("REDDIT"),

  TWITTER("TWITTER");

  private String value;

  Channel(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Channel fromValue(String value) {
    for (Channel b : Channel.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

