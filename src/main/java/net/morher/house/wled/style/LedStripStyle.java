package net.morher.house.wled.style;

import java.util.function.Consumer;
import lombok.Data;
import net.morher.house.api.utils.color.Color;

@Data
public class LedStripStyle {
  private Color color1;
  private Color color2;
  private Color color3;
  private Integer effect;
  private Integer speed;
  private Integer intensity;
  private Integer palette;

  public void apply(LedStripStyle state) {
    useIfNotNull(state.getColor1(), this::setColor1);
    useIfNotNull(state.getColor2(), this::setColor2);
    useIfNotNull(state.getColor3(), this::setColor3);
    useIfNotNull(state.getEffect(), this::setEffect);
    useIfNotNull(state.getSpeed(), this::setSpeed);
    useIfNotNull(state.getIntensity(), this::setIntensity);
    useIfNotNull(state.getPalette(), this::setPalette);
  }

  static <T> void useIfNotNull(T value, Consumer<T> setter) {
    if (value != null) {
      setter.accept(value);
    }
  }
}
