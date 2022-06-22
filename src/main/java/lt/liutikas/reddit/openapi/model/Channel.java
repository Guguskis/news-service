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
 * Gets or Sets Channel
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T18:52:21.870437300+03:00[Europe/Vilnius]")
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

