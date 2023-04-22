package net.morher.house.wled.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import io.javalin.http.staticfiles.StaticFileConfig;
import io.javalin.plugin.json.JavalinJackson;
import java.io.InputStream;
import net.morher.house.wled.WledControllerImpl;
import net.morher.house.wled.frontend.auth.Role;
import net.morher.house.wled.frontend.auth.UserManager;
import org.jetbrains.annotations.NotNull;

/** @author Morten Hermansen */
public class WledFrontendServer {
  private final UserHandler userHandler;
  private final DeviceHandler deviceHandler;
  private final UserManager userManager;

  public WledFrontendServer(WledControllerImpl controller, UserManager userManager) {
    this.userHandler = new UserHandler(controller);
    this.deviceHandler = new DeviceHandler(controller);
    this.userManager = userManager;
  }

  public void run() {
    Javalin app = Javalin.create(this::config).start(7070);
    app.before("/d/<deviceName>/*", deviceHandler::beforeDevice);
    app.get("/d/<deviceName>/data", deviceHandler::getDeviceData, Role.AUTHENTICATED);
    app.get("/d/<deviceName>/effects", deviceHandler::getDeviceEffects, Role.AUTHENTICATED);
    app.post("/d/<deviceName>/lamp", deviceHandler::setLampState, Role.AUTHENTICATED);
    app.get("/d/<deviceName>/palettes", deviceHandler::getDevicePalettes, Role.AUTHENTICATED);
    app.get("/d/<deviceName>/state", deviceHandler::getDeviceState, Role.AUTHENTICATED);
    app.post("/d/<deviceName>/state", deviceHandler::setDeviceState, Role.AUTHENTICATED);
    app.get("/d/<deviceName>/", this::getDeviceControlPanel, Role.AUTHENTICATED);
    app.get("/user/", userHandler::getUserProfile, Role.USER);
  }

  private void getDeviceControlPanel(@NotNull Context ctx) {
    InputStream controlPanelHtml = getClass().getResourceAsStream("/public/index.html");
    ctx.contentType(ContentType.HTML);
    ctx.result(controlPanelHtml);
  }

  private void config(JavalinConfig config) {
    config.addStaticFiles(this::config);
    config.accessManager(userManager);
    config.jsonMapper(new JavalinJackson(new ObjectMapper()));
  }

  private void config(StaticFileConfig config) {
    config.hostedPath = "/";
    config.directory = "/public";
    config.location = Location.CLASSPATH;
  }
}
