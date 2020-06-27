package com.gmail.chickenpowerrr.ranksync.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
    map.forEach((key, values) -> values.forEach(value -> {
      result.computeIfAbsent(value, v -> new HashSet<>());
      result.get(value).add(key);
    }));
    return result;
  }

  /**
   * Allows checked {@link Exception}s to be thrown as if they
   * were a {@link RuntimeException}.
   *
   * @param t the {@link Throwable} which should be thrown
   * @param <T> the type of the {@link Throwable}
   * @param <U> the fake return type
   * @return nothing
   * @throws T the provide {@link Throwable}
   */
  @SuppressWarnings("unchecked")
  @Contract(pure = true)
  public static <T extends Throwable, U> U sneakyThrow(@NotNull Throwable t) throws T {
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
  @NotNull
  public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
    Map<K, V> map = new HashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    return map;
  }

  /**
   * Prevents instantiation of this class.
   */
  private Util() {

  }
}
