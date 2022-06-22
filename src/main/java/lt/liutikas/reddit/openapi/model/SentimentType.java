package lt.liutikas.reddit.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets SentimentType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T17:31:34.079492+03:00[Europe/Vilnius]")
public enum SentimentType {
  
  POSITIVE("POSITIVE"),
  
  NEGATIVE("NEGATIVE"),
  
  NEUTRAL("NEUTRAL"),
  
  MIXED("MIXED");

  private String value;

  SentimentType(String value) {
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
  public static SentimentType fromValue(String value) {
    for (SentimentType b : SentimentType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

