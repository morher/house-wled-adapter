package net.morher.house.wled.frontend.to;

import lombok.Data;
import net.morher.house.api.utils.color.Color;
import net.morher.house.wled.style.LedStripStyle;

@Data
public class LedStyleTO {
  private Color color1;

  private Color color2;

  private Color color3;

  private Integer effect;

  private Integer speed;

  private Integer intensity;

  private Integer palette;

  public static LedStyleTO from(LedStripStyle style) {
    LedStyleTO to = new LedStyleTO();
    if (style != null) {
      to.setColor1(style.getColor1());
      to.setColor2(style.getColor2());
      to.setColor3(style.getColor3());
      to.setEffect(style.getEffect());
      to.setSpeed(style.getSpeed());
      to.setIntensity(style.getIntensity());
      to.setPalette(style.getPalette());
    }
    return to;
  }
}
