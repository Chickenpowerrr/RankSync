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
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, Object value);

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, String value);

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, int value);

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, double value);

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, long value);

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  T addProperty(String key, boolean value);

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  Object getObject(String key);

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  String getString(String key);

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  int getInt(String key);

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  double getDouble(String key);

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  long getLong(String key);

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  boolean getBoolean(String key);

  /**
   * Returns if the cache contains a given key
   *
   * @param key the key that is used to save and request the data
   * @return true if the cache contains the given key, false if not
   */
  boolean has(String key);

  /**
   * Returns if the cache contains a multiple given keys
   *
   * @param keys the keys that are used to save and request the data
   * @return true if the cache contains all of the given keys, false if not
   */
  boolean has(String... keys);
}
