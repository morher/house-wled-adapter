package net.morher.house.wled.frontend.auth;

import io.javalin.http.Context;
import io.javalin.http.Cookie;
import java.util.Collection;
import java.util.HashSet;
import net.morher.house.wled.frontend.RequestAttribute;

public class Tokens {
  public static RequestAttribute<Collection<String>> AVAILABLE_TOKENS =
      new RequestAttribute<>("available-tokens", new HashSet<>());

  public static void handle(Context ctx, String cookiePath) {
    String token = ctx.queryParam("token");
    if (token != null) {
      ctx.cookie(new Cookie("token", token, cookiePath));
    }
  }

  public static void addAvailableToken(Context ctx, String token) {
    if (token != null) {
      AVAILABLE_TOKENS.get(ctx).add(token);
    }
  }

  public static boolean hasValidToken(Context ctx) {
    String token = ctx.cookie("token");
    if (token == null) {
      token = ctx.queryParam("token");
    }
    return token != null && AVAILABLE_TOKENS.get(ctx).contains(token);
  }
}
