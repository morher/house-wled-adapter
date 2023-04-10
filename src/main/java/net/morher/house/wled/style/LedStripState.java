package net.morher.house.wled.style;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LedStripState extends LedStripStyle {
  private Boolean powerOn;
  private Integer brightness;

  @JsonInclude(NON_NULL)
  private Boolean selected;

  public void apply(LedStripState state, boolean styleOnly) {
    if (!styleOnly) {
      useIfNotNull(state.getPowerOn(), this::setPowerOn);
      useIfNotNull(state.getBrightness(), this::setBrightness);
    }
    super.apply(state);
  }
}
