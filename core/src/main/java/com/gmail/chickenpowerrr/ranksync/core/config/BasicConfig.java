package com.gmail.chickenpowerrr.ranksync.core.config;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This {@link Config} uses a {@link HashMap} to store the
 * values, meaning that everything will be discarded when
 * the applications shuts down.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public final class BasicConfig extends AbstractConfig {

  /**
   * Constructs a new {@link Config} without any content.
   */
  public BasicConfig() {
    super(new CacheValueProvider());
  }

  /**
   * This {@link ValueProvider} uses a {@link HashMap}
   * to store and retrieve its values.
   *
   * @author Mark van Wijk
   * @since 2.0.0
   */
  private static final class CacheValueProvider implements ValueProvider {

    private final Map<String, Object> values;

    /**
     * Constructs an empty {@link ValueProvider}.
     */
    private CacheValueProvider() {
      this.values = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Nullable
    @Override
    public Object get(@NotNull String path) {
      return this.values.get(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(@NotNull String path, @Nullable Object value) {
      this.values.put(path, value);
    }
  }
}
