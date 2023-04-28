package net.morher.house.wled.presets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.morher.house.wled.style.LedStripStyle;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Preset {
  private String name;
  private LedStripStyle state;
}
