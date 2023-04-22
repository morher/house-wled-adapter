package net.morher.house.wled.frontend;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static net.morher.house.api.entity.light.LightState.PowerState.OFF;
import static net.morher.house.api.entity.light.LightState.PowerState.ON;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.morher.house.api.entity.light.LightState;
import net.morher.house.wled.frontend.to.LedStyleTO;

@Data
@JsonInclude(content = NON_EMPTY)
public class LedStripDataTO {

  private LedStyleTO style;

  private LampStateTO lamp;

  private final List<PresetTO> presets = new ArrayList<>();

  @Data
  public static class LampStateTO {
    private Boolean powerOn;
    private Integer brightness;
    private String preset;

    public static LampStateTO from(LightState state) {
      LampStateTO to = new LampStateTO();
      to.setPowerOn(ON.equals(state.getState()));
      to.setBrightness(state.getBrightness());
      to.setPreset(state.getEffect());
      return to;
    }

    public LightState asLightState() {
      return new LightState(powerOn ? ON : OFF, brightness, preset);
    }
  }

  @Data
  @AllArgsConstructor
  @RequiredArgsConstructor
  public static class PresetTO {
    private String name;
  }
}
