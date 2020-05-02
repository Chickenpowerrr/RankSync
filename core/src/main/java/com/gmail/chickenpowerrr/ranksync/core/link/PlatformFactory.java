package com.gmail.chickenpowerrr.ranksync.core.link;

import com.gmail.chickenpowerrr.ranksync.core.config.Config;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlatformFactory {

  private final Map<String, Function<Config, Platform<?>>> constructors;

  public PlatformFactory() {
    this.constructors = new HashMap<>();
  }

  @SuppressWarnings("unchecked")
  @Contract("null, _ -> null; _, null -> null")
  public <P extends Platform<P>> P getPlatform(@Nullable String name, @Nullable Config config) {
    if (this.constructors.containsKey(name) && config != null) {
      return (P) this.constructors.get(name).apply(config);
    } else {
      return null;
    }
  }

  public void register(@NotNull String name, @NotNull Function<Config, Platform<?>> constructor) {
    this.constructors.put(name, constructor);
  }
}
