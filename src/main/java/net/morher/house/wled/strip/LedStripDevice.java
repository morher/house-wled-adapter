package net.morher.house.wled.strip;

import java.util.Objects;
import java.util.function.Consumer;
import kotlin.jvm.Synchronized;
import lombok.Getter;
import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.entity.light.LightOptions;
import net.morher.house.api.entity.light.LightState;
import net.morher.house.api.entity.light.LightState.PowerState;
import net.morher.house.api.entity.light.LightStateHandler;
import net.morher.house.api.mqtt.client.TopicValueHandler;
import net.morher.house.api.mqtt.payload.JsonMessage;
import net.morher.house.api.mqtt.payload.RawMessage;
import net.morher.house.api.utils.ResourceManager;
import net.morher.house.wled.presets.EffectManager;
import net.morher.house.wled.presets.Preset;
import net.morher.house.wled.style.LedStripState;
import net.morher.house.wled.style.LedStripStyle;

public class LedStripDevice {
  private final Consumer<LedStripState> listener;
  private final ResourceManager resources = new ResourceManager();
  private final LightStateHandler lightHandler;
  private final EffectManager presets;
  private LightState lightState = new LightState();
  @Getter private LedStripState state = new LedStripState();
  private final TopicValueHandler<LedStripStyle> customStyle;
  private final TopicValueHandler<String> lastPreset;

  public LedStripDevice(
      Consumer<LedStripState> listener, LightEntity lightEntity, EffectManager presets) {
    this.listener = listener;
    this.presets = presets;

    resources.add(
        customStyle =
            lightEntity
                .state()
                .subTopic("/wled-custom-style", JsonMessage.toType(LedStripStyle.class), true)
                .valueHandler(this::onCustomStyle));

    resources.add(
        lastPreset =
            lightEntity
                .state()
                .subTopic("/wled-last-preset", RawMessage.toStr(), true)
                .observer(presets.defaultPreset()));

    this.lightHandler = new LightStateHandler(lightEntity, this::onLightState);
    lightHandler.updateOptions(new LightOptions(true, presets.getEffectNames()));
  }

  public void onLightState(LightState state) {
    String effect = state.getEffect();
    if (presets.isLastPreset(effect)) {
      effect = lastPreset.get();
      if (effect != null) {
        state = lightState.withEffect(effect);
        lightHandler.updateState(state);
      }
    } else if (effect != null && !Objects.equals(presets.defaultPreset(), effect)) {
      lastPreset.publish(effect);
    }
    lightState = state;
    updateState();
  }

  public void onCustomStyle(LedStripStyle style) {
    updateState();
  }

  public void updateCustomStyle(LedStripStyle newStyle) {
    LedStripStyle style = new LedStripStyle();
    style.apply(newStyle);
    customStyle.publish(style);
    lightState = lightState.withEffect(presets.getCustomEffect());
    lightHandler.updateState(lightState);
    updateState();
  }

  private void updateState() {
    state = calculateState();
    listener.accept(state);
  }

  @Synchronized
  private LedStripState calculateState() {
    LedStripState state = new LedStripState();
    if (lightState.getState() != null) {
      state.setPowerOn(PowerState.ON.equals(lightState.getState()));
    }
    if (lightState.getBrightness() != null) {
      state.setBrightness(lightState.getBrightness());
    }
    if (presets.isCustomEffect(lightState.getEffect())) {
      LedStripStyle style = customStyle.get();
      if (style != null) {
        state.apply(style);
      }
    } else {
      Preset preset = getPreset(lightState.getEffect());
      if (preset != null) {
        state.apply(preset.getState());
      }
    }
    return state;
  }

  private Preset getPreset(String presetId) {
    for (Preset preset : presets.getLedStripPresets()) {
      if (preset.getName().equals(presetId)) {
        return preset;
      }
    }
    return null;
  }
}
