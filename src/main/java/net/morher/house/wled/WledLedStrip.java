package net.morher.house.wled;

import lombok.Getter;
import net.morher.house.api.devicetypes.LampDevice;
import net.morher.house.api.entity.Device;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.utils.ResourceManager;
import net.morher.house.wled.presets.EffectManager;
import net.morher.house.wled.strip.LedStripDevice;
import net.morher.house.wled.style.LedStripState;

public class WledLedStrip {
  private final ResourceManager resources = new ResourceManager();
  @Getter private final String id;
  private final Device device;
  private final WledNode node;
  private final int segmentId;
  @Getter private final String token;
  private final LedStripDevice stripDevice;

  public WledLedStrip(
      String id, Device device, WledNode node, int segmentId, String token, EffectManager presets) {

    this.id = id;
    this.device = device;
    this.node = node;
    this.segmentId = segmentId;
    this.token = token;

    DeviceInfo deviceInfo = new DeviceInfo();
    deviceInfo.setManufacturer("Wled");
    device.setDeviceInfo(deviceInfo);

    LightEntity lightEntity = device.entity(LampDevice.LIGHT);

    this.stripDevice = new LedStripDevice(this::onLedStripStateUpdate, lightEntity, presets);
  }

  public void onLedStripStateUpdate(LedStripState state) {
    node.updateSegment(segmentId, state);
  }

  public DeviceId getDeviceId() {
    return device.getId();
  }

  public LedStripState getState() {
    return stripDevice.getState();
  }

  public void setState(LedStripState state) {
    stripDevice.updateCustomStyle(state);
  }
}
