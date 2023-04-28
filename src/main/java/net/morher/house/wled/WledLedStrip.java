package net.morher.house.wled;

import static java.util.Collections.emptyList;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.morher.house.api.devicetypes.LampDevice;
import net.morher.house.api.entity.Device;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.entity.light.LightState;
import net.morher.house.api.utils.ResourceManager;
import net.morher.house.wled.presets.EffectManager;
import net.morher.house.wled.strip.LedStripDevice;
import net.morher.house.wled.strip.WledSegment;
import net.morher.house.wled.style.LedStripState;

public class WledLedStrip {
  private final ResourceManager resources = new ResourceManager();
  @Getter private final String id;
  private final Device device;
  private final LightEntity lightEntity;
  @Getter private final String token;
  private final LedStripDevice stripDevice;
  @Getter private final List<WledSegment> segments = new ArrayList<>();

  public WledLedStrip(String id, Device device, String token, EffectManager presets) {

    this.id = id;
    this.device = device;
    this.token = token;

    DeviceInfo deviceInfo = new DeviceInfo();
    deviceInfo.setManufacturer("Wled");
    device.setDeviceInfo(deviceInfo);

    lightEntity = device.entity(LampDevice.LIGHT);

    this.stripDevice = new LedStripDevice(this::onLedStripStateUpdate, lightEntity, presets);
  }

  public void onLedStripStateUpdate(LedStripState state) {
    segments.forEach(s -> s.updateState(state));
  }

  public DeviceId getDeviceId() {
    return device.getId();
  }

  public LedStripState getState() {
    return stripDevice.getState();
  }

  public LightState getLampState() {
    return stripDevice.getLightState();
  }

  public void setState(LedStripState state) {
    stripDevice.updateCustomStyle(state);
  }

  public void setLampState(LightState state) {
    stripDevice.setLightState(state);
  }

  public List<String> getPresets() {
    return lightEntity.getOptions().getEffects() != null
        ? lightEntity.getOptions().getEffects()
        : emptyList();
  }
}
