package com.gmail.chickenpowerrr.ranksync.core.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface defines the basic interactions a
 * {@link Config} should be able to make with a
 * provider.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public interface ValueProvider {

  /**
   * Returns the {@link Object} which has been stored at the
   * given path.
   *
   * @param path the unique path to this {@link Object}
   * @return the {@link Object} stored at the given path,
   *         {@code null} if there is no such value
   */
  @Nullable
  @Contract(pure = true)
  Object get(@NotNull String path);

  /**
   * Stores a given {@link Object} at a given path, to make sure
   * that it will be the result when {@link #get(String)} gets
   * called with the provided path.
   *
   * @param path the unique path to this {@link Object}
   * @param value the {@link Object} which should be stored at
   *              the given path
   */
  void set(@NotNull String path, @Nullable Object value);
}
