package net.morher.house.wled.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WledNodeState {

  @JsonProperty("on")
  private Boolean powerOn;

  @JsonProperty("bri")
  private Integer brightness;

  @JsonProperty("transition")
  private Integer transition;

  @JsonProperty("ps")
  private Integer preset;

  @JsonProperty("seg")
  private List<WledSegment> segments;

  @JsonProperty("v")
  private Boolean verboseResponse;

  public WledSegment getSegment(int segmentId) {
    if (segments != null) {
      for (WledSegment segment : segments) {
        if (segment.getId() != null && segmentId == segment.getId()) {
          return segment;
        }
      }
    }
    return null;
  }
}
