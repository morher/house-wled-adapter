package net.morher.house.wled.frontend;

import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import net.morher.house.wled.WledControllerImpl;
import net.morher.house.wled.WledLedStrip;
import net.morher.house.wled.frontend.auth.Tokens;
import net.morher.house.wled.style.LedStripState;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DeviceHandler {
  private final RequestAttribute<WledLedStrip> DEVICE = new RequestAttribute<>("device");
  private final WledControllerImpl controller;

  public void getDeviceState(@NotNull Context ctx) {

    String stripId = ctx.pathParam("deviceName");
    ctx.json(controller.getStrip(stripId).getState());
  }

  public void beforeDevice(@NotNull Context ctx) {
    String stripId = ctx.pathParam("deviceName");
    try {
      WledLedStrip strip = controller.getStrip(stripId);
      DEVICE.set(ctx, strip);
      Tokens.handle(ctx, "/d/" + stripId);
      Tokens.addAvailableToken(ctx, strip.getToken());
    } catch (IllegalArgumentException e) {
      throw new NotFoundResponse("LED strip " + stripId + " not found");
    }
  }

  public void setDeviceState(@NotNull Context ctx) {
    String stripId = ctx.pathParam("deviceName");
    LedStripState state = ctx.bodyAsClass(LedStripState.class);
    controller.getStrip(stripId).setState(state);
    ctx.result("Ok");
  }

  public void getDeviceEffects(@NotNull Context ctx) {
    InputStream effectsJson = getClass().getResourceAsStream("/effects.json");
    ctx.contentType(ContentType.JSON);
    ctx.result(effectsJson);
  }

  public void getDevicePalettes(@NotNull Context ctx) {
    InputStream effectsJson = getClass().getResourceAsStream("/palettes.json");
    ctx.contentType(ContentType.JSON);
    ctx.result(effectsJson);
  }

  public void checkDeviceAccess(String stripId) {}
}
