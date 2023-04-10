package net.morher.house.wled.presets;

import static java.util.stream.Collectors.toList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.morher.house.wled.config.WledConfiguration.WledPresetConfig;

public interface EffectManager {
  void configure(Map<String, WledPresetConfig> presets);

  List<Preset> getLedStripPresets();

  default boolean isLastPreset(String effectName) {
    return "Last preset".equals(effectName);
  }

  default boolean isCustomEffect(String effectName) {
    return "Custom style".equals(effectName);
  }

  default String defaultPreset() {
    return "Auto";
  }

  default String getCustomEffect() {
    return "Custom style";
  }

  default String getLastPresetName() {
    return "Last preset";
  }

  default List<String> getEffectNames() {
    ArrayList<String> effectNames = new ArrayList<>();
    effectNames.add(defaultPreset());
    effectNames.addAll(getLedStripPresets().stream().map(Preset::getName).collect(toList()));
    effectNames.add(getCustomEffect());
    effectNames.add(getLastPresetName());
    return effectNames;
  }
}
