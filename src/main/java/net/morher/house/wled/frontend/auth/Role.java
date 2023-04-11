package net.morher.house.wled.frontend.auth;

import io.javalin.core.security.RouteRole;

public enum Role implements RouteRole {
  ANYONE,
  AUTHENTICATED,
  USER,
  ADMIN;
}
