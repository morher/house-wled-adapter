package net.morher.house.wled.frontend.auth;

import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;

public class LoginExceptionHandler implements ExceptionHandler<InvalidAuthException> {

  @Override
  public void handle(InvalidAuthException exception, Context ctx) {}
}
