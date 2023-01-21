package net.morher.house.wled.frontend;

import net.morher.house.api.utils.color.Color;
import net.morher.house.wled.LedStripState;

public class LedStripStateTO {
  public Boolean powerOn;

  public Integer brightness;

  public Color color1;

  public Color color2;

  public Color color3;

  public Integer effect;

  public Integer speed;

  public Integer intensity;

  public Integer palette;

  public Boolean selected;

  public static LedStripStateTO from(LedStripState state) {
    return new LedStripStateTO();
  }
}
