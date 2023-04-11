package net.morher.house.wled.frontend.auth;

import io.javalin.core.security.AccessManager;
import io.javalin.http.Context;
import net.morher.house.wled.frontend.RequestAttribute;

public interface UserManager extends AccessManager {
  public static RequestAttribute<String> USERNAME = new RequestAttribute<>("authenticated-user");

  String getUser(Context ctx) throws InvalidAuthException;
}
