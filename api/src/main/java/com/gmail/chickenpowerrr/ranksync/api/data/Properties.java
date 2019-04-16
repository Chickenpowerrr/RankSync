package com.gmail.chickenpowerrr.ranksync.api.data;

/**
 * This interface defines all of the basic things an Object needs to save very basic data
 *
 * @param <T> the class that implements this interface
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface Properties<T extends Properties> extends Cloneable {

  /**
   * Add a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, Object value);

  /**
   * Add a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, String value);

  /**
   * Add a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, int value);

  /**
   * Add a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, double value);

  /**
   * Add a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, long value);

  /**
   * Get a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  Object getObject(String key);

  /**
   * Get a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  String getString(String key);

  /**
   * Get a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  int getInt(String key);

  /**
   * Get a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  double getDouble(String key);

  /**
   * Get a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  long getLong(String key);

  /**
   * Tells if the cache contains a given key
   *
   * @param key the key that is used to save and request the data
   * @return true if the cache contains the given key, false if not
   */
  boolean has(String key);

  /**
   * Tells if the cache contains a multiple given keys
   *
   * @param keys the keys that are used to save and request the data
   * @return true if the cache contains all of the given keys, false if not
   */
  boolean has(String... keys);
}
