package net.morher.house.wled.frontend;

import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestAttribute<T> {
  private final String attributeKey;
  private final T defaultValue;

  public RequestAttribute(String attributeKey) {
    this(attributeKey, null);
  }

  public void set(Context ctx, T value) {
    ctx.attribute(attributeKey, value);
  }

  public T get(Context ctx) {
    T value = ctx.attribute(attributeKey);
    if (value == null && defaultValue != null) {
      value = defaultValue;
      ctx.attribute(attributeKey, value);
    }
    return value;
  }
}
