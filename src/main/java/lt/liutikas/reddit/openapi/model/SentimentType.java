package lt.liutikas.reddit.openapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Generated;

/**
 * Gets or Sets SentimentType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T09:12:23.505559700+03:00[Europe/Vilnius]")
public enum SentimentType {

  POSITIVE("POSITIVE"),

  NEGATIVE("NEGATIVE"),

  NEUTRAL("NEUTRAL"),

  MIXED("MIXED");

  private String value;

  SentimentType(String value) {
    this.value = value;
  }

  @JsonCreator
  public static SentimentType fromValue(String value) {
    for (SentimentType b : SentimentType.values()) {
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

