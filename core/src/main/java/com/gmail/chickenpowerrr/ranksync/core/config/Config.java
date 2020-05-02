package com.gmail.chickenpowerrr.ranksync.core.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface defines the basic interactions an
 * application should be able to make in order to
 * interact with a certain config.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public interface Config {

  /**
   * Returns {@code true} if there exists a value at the
   * given path, {@code false} otherwise.
   *
   * @param path the path to be checked for a value
   * @return {@code true} if there exists a value at the
   *         given path, {@code false} otherwise
   */
  @Contract(value = "null -> false", pure = true)
  boolean contains(@Nullable String path);

  /**
   * Returns the {@link Object} stored at the given path.
   *
   * @param path the unique path to the {@link Object}
   * @return the {@link Object} stored at the given path
   */
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
  @Contract(pure = false)
  void set(@NotNull String path, @Nullable Object value);

  /**
   * Returns the {@link String} stored at the given path.
   *
   * @param path the unique path to the {@link String}
   * @return the {@link String} stored at the given path
   */
  @Contract(pure = true)
  String getString(@NotNull String path);

  /**
   * Stores a given {@link String} at a given path, to make sure
   * that it will be the result when {@link #getString(String)} gets
   * called with the provided path.
   *
   * @param path the unique path to this {@link String}
   * @param value the {@link String} which should be stored at
   *              the given path
   */
  @Contract(pure = false)
  void setString(@NotNull String path, @Nullable String value);

  /**
   * Returns {@code true} if the value stored at the given given
   * is an {@link Integer}, {@code false} otherwise.
   *
   * @param path the unique path to this {@link Integer}
   * @return {@code true} if the value stored at the given given
   *         is an {@link Integer}, {@code false} otherwise
   */
  @Contract(value = "null -> false", pure = true)
  boolean isInt(@Nullable String path);

  /**
   * Returns the {@link Integer} stored at the given path.
   *
   * @param path the unique path to the {@link Integer}
   * @return the {@link Integer} stored at the given path
   */
  @Contract(pure = true)
  int getInt(@NotNull String path);

  /**
   * Stores a given {@link Integer} at a given path, to make sure
   * that it will be the result when {@link #getInt(String)} gets
   * called with the provided path.
   *
   * @param path the unique path to this {@link Integer}
   * @param value the {@link Integer} which should be stored at
   *              the given path
   */
  @Contract(pure = false)
  void setInt(@NotNull String path, int value);

  /**
   * Returns {@code true} if the value stored at the given given
   * is an {@link Double}, {@code false} otherwise.
   *
   * @param path the unique path to this {@link Double}
   * @return {@code true} if the value stored at the given given
   *         is an {@link Double}, {@code false} otherwise
   */
  @Contract(pure = true)
  boolean isDouble(@Nullable String path);

  /**
   * Returns the {@link Double} stored at the given path.
   *
   * @param path the unique path to the {@link Double}
   * @return the {@link Double} stored at the given path
   */
  @Contract(pure = true)
  double getDouble(@NotNull String path);

  /**
   * Stores a given {@link Double} at a given path, to make sure
   * that it will be the result when {@link #getDouble(String)} gets
   * called with the provided path.
   *
   * @param path the unique path to this {@link Double}
   * @param value the {@link Double} which should be stored at
   *              the given path
   */
  @Contract(pure = false)
  void setDouble(@NotNull String path, double value);

  /**
   * Returns {@code true} if the value stored at the given given
   * is an {@link Long}, {@code false} otherwise.
   *
   * @param path the unique path to this {@link Long}
   * @return {@code true} if the value stored at the given given
   *         is an {@link Long}, {@code false} otherwise
   */
  @Contract(pure = true)
  boolean isLong(@Nullable String path);

  /**
   * Returns the {@link Long} stored at the given path.
   *
   * @param path the unique path to the {@link Long}
   * @return the {@link Long} stored at the given path
   */
  @Contract(pure = true)
  long getLong(@NotNull String path);

  /**
   * Stores a given {@link Long} at a given path, to make sure
   * that it will be the result when {@link #getLong(String)} gets
   * called with the provided path.
   *
   * @param path the unique path to this {@link Long}
   * @param value the {@link Long} which should be stored at
   *              the given path
   */
  @Contract(pure = false)
  void setLong(@NotNull String path, long value);

  /**
   * Returns {@code true} if the value stored at the given given
   * is an {@link Boolean}, {@code false} otherwise.
   *
   * @param path the unique path to this {@link Boolean}
   * @return {@code true} if the value stored at the given given
   *         is an {@link Boolean}, {@code false} otherwise
   */
  @Contract(pure = true)
  boolean isBoolean(@Nullable String path);

  /**
   * Returns the {@link Boolean} stored at the given path.
   *
   * @param path the unique path to the {@link Boolean}
   * @return the {@link Boolean} stored at the given path
   */
  @Contract(pure = true)
  boolean getBoolean(@NotNull String path);

  /**
   * Stores a given {@link Boolean} at a given path, to make sure
   * that it will be the result when {@link #getBoolean(String)} gets
   * called with the provided path.
   *
   * @param path the unique path to this {@link Boolean}
   * @param value the {@link Boolean} which should be stored at
   *              the given path
   */
  @Contract(pure = false)
  void setBoolean(@NotNull String path, boolean value);
}
