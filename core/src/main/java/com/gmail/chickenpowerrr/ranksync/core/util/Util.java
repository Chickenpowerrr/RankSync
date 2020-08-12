package com.gmail.chickenpowerrr.ranksync.core.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class contains simple utilities which can be
 * used in the rest of the code.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class Util {

  /**
   * Interchanges the keys and the values of a {@link Map}.
   *
   * @param map the map which needs its values and keys interchanged
   * @param <K> the type of the keys
   * @param <V> the type of the values
   * @return the interchanged map
   */
  @NotNull
  @Contract(pure = true)
  public static <K, V> Map<V, Collection<K>> interchange(@NotNull Map<K, Collection<V>> map) {
    Map<V, Collection<K>> result = new HashMap<>();
    map.forEach((key, values) -> values.forEach(value ->
      result.computeIfAbsent(value, v -> new HashSet<>()).add(key)));
    return result;
  }

  /**
   * A Java hack which tricks the compiler into believing a certain {@link Throwable} is
   * an unchecked exception, while in reality, it's a checked exception, if a {@link Throwable}
   * is thrown, otherwise the regular result will be returned.
   *
   * @param callable the callable which will contain the result or throw the {@link Throwable}
   * @param <U> the return type
   * @return the result of the given callable
   */
  @Contract(pure = true)
  public static <U> U sneakyExecute(@NotNull Callable<U> callable) {
    try {
      return callable.call();
    } catch (Exception e) {
      return sneakyThrow(e);
    }
  }

  /**
   * A Java hack which tricks the compiler into believing a certain {@link Throwable} is
   * an unchecked exception, while in reality, it's a checked exception.
   *
   * @param t the {@link Throwable} which needs to be thrown
   * @param <T> the type of the throwable
   * @param <U> the return type the Java method wants
   * @return nothing
   * @throws T the given {@link Throwable}
   */
  @Contract(pure = true)
  @SuppressWarnings("unchecked")
  public static <T extends Throwable, U> U sneakyThrow(Throwable t) throws T {
    throw (T) t;
  }

  /**
   * Casts a {@link Object} to a wanted type.
   *
   * @param o the {@link Object} to be casted
   * @param <T> the required type
   * @return the given {@link Object}
   */
  @SuppressWarnings("unchecked")
  @Contract(value = "!null -> !null; null -> null", pure = true)
  public static <T> T cast(@Nullable Object o) {
    return (T) o;
  }

  /**
   * Maps a {@link Collection} of {@link Collection}s to just
   * on, complete {@link Collection}.
   *
   * @param collections the {@link Collection}s which should be mapped
   *                    to just one {@link Collection}
   * @param <T> the type of the content of the {@link Collection}s
   * @return the general {@link Collection}
   */
  @Contract(pure = true)
  @NotNull
  public static <T> Collection<T> flatMap(@NotNull Collection<Collection<T>> collections) {
    Collection<T> result = new HashSet<>();
    for (Collection<T> collection : collections) {
      result.addAll(collection);
    }
    return result;
  }

  /**
   * Returns a {@link Map} with the given key and value
   * pairs.
   *
   * @param k1 the first key
   * @param v1 the first value
   * @param <K> the type of the keys
   * @param <V> the type of the values
   * @return the {@link Map} with the given key value pairs
   */
  @Contract(pure = true)
  @NotNull
  public static <K, V> Map<K, V> mapOf(K k1, V v1) {
    Map<K, V> map = new HashMap<>();
    map.put(k1, v1);
    return map;
  }

  /**
   * Returns a {@link Map} with the given key and value
   * pairs.
   *
   * @param k1 the first key
   * @param v1 the first value
   * @param k2 the second key
   * @param v2 the second value
   * @param <K> the type of the keys
   * @param <V> the type of the values
   * @return the {@link Map} with the given key value pairs
   */
  @Contract(pure = true)
  @NotNull
  public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
    Map<K, V> map = new HashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    return map;
  }

  /**
   * Returns a {@link Set} with the given values.
   *
   * @param values the values which will be in the {@link Set}
   * @param <T> the type of the values in the {@link Set}
   * @return the {@link Set} filled with the given values
   */
  @Contract(pure = true)
  @SafeVarargs
  @NotNull
  public static <T> Set<T> setOf(T... values) {
    Set<T> result = new HashSet<>();
    Collections.addAll(result, values);
    return result;
  }

  /**
   * Prevents instantiation of this class.
   */
  private Util() {

  }
}
