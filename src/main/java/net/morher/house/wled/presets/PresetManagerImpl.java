package net.morher.house.wled.presets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.morher.house.api.utils.color.Color;
import net.morher.house.wled.config.WledConfiguration.WledPresetConfig;
import net.morher.house.wled.config.WledConfiguration.WledPresetStateConfig;
import net.morher.house.wled.style.LedStripState;

public class PresetManagerImpl implements EffectManager {
  private List<Preset> presets = new ArrayList<>();

  public PresetManagerImpl() {}

  public void configure(Map<String, WledPresetConfig> presetConfigs) {
    this.presets.clear();
    if (presetConfigs != null) {
      presetConfigs.forEach(this::addPreset);
    }
  }

  private void addPreset(String name, WledPresetConfig presetConfig) {
    LedStripState state = new LedStripState();
    WledPresetStateConfig stateConfig = presetConfig.getStyle();
    List<Color> colors = stateConfig.getColors();
    if (colors.size() > 0) {
      state.setColor1(colors.get(0));
    }
    if (colors.size() > 1) {
      state.setColor2(colors.get(1));
    }
    if (colors.size() > 2) {
      state.setColor3(colors.get(2));
    }
    state.setPalette(stateConfig.getPalette());
    state.setIntensity(stateConfig.getIntensity());
    state.setSpeed(stateConfig.getSpeed());
    state.setEffect(stateConfig.getEffect());
    presets.add(new Preset(name, state));
  }

  @Override
  public List<Preset> getLedStripPresets() {
    return presets;
  }
}
