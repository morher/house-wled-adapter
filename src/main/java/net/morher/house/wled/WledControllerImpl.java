package net.morher.house.wled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import net.morher.house.api.entity.Device;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceManager;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.payload.JsonMessage;
import net.morher.house.api.schedule.HouseScheduler;
import net.morher.house.wled.config.WledConfiguration;
import net.morher.house.wled.config.WledConfiguration.WledLampConfig;
import net.morher.house.wled.presets.EffectManager;
import net.morher.house.wled.presets.PresetManagerImpl;
import net.morher.house.wled.to.WledNodeState;

public class WledControllerImpl {
  private final ScheduledExecutorService scheduler = HouseScheduler.get();
  private final HouseMqttClient mqtt;
  private final DeviceManager deviceManager;
  private final Map<String, WledLedStrip> strips = new HashMap<>();
  private final EffectManager presets = new PresetManagerImpl();

  public WledControllerImpl(HouseMqttClient mqtt, DeviceManager deviceManager) {
    this.mqtt = mqtt;
    this.deviceManager = deviceManager;
  }

  public void configure(WledConfiguration config) {
    scheduler.execute(() -> performConfiguration(config));
  }

  private void performConfiguration(WledConfiguration config) {
    presets.configure(config.getPresets());

    for (WledLampConfig lampConfig : config.getLamps()) {
      String id = lampConfig.getId();

      DeviceId deviceId = lampConfig.getDevice().toDeviceId();
      Device device = deviceManager.device(deviceId);

      WledNode node = findOrCreateNode(lampConfig.getTopic());
      WledLedStrip strip = new WledLedStrip(id, device, node, lampConfig.getSegment(), presets);

      strips.put(id, strip);
    }
  }

  private WledNode findOrCreateNode(String topic) {
    return new WledNode(mqtt.topic(topic + "/api", JsonMessage.toType(WledNodeState.class)));
  }

  public WledLedStrip getStrip(String stripId) {
    WledLedStrip strip = strips.get(stripId);
    if (strip == null) {
      throw new IllegalArgumentException("Fant ikke wled-strip: " + stripId);
    }
    return strip;
  }

  public List<WledLedStrip> getStrips() {
    return new ArrayList<>(strips.values());
  }
}
