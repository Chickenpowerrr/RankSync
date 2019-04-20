package com.gmail.chickenpowerrr.ranksync.api.data;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is one of the easiest possible implementations of the Properties interface. It can be
 * used to add database credentials to a constructors without having to specifically define every
 * single parameter
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@EqualsAndHashCode
public class BasicProperties implements Properties<BasicProperties> {

  private final Map<String, Object> properties;

  /**
   * Creates a new, empty instance of the Properties
   */
  public BasicProperties() {
    this(new HashMap<>());
  }

  private BasicProperties(Map<String, Object> properties) {
    this.properties = properties;
  }

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  @Override
  public BasicProperties addProperty(String key, int value) {
    this.properties.put(key, value);
    return this;
  }

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  @Override
  public BasicProperties addProperty(String key, double value) {
    this.properties.put(key, value);
    return this;
  }

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  @Override
  public BasicProperties addProperty(String key, Object value) {
    this.properties.put(key, value);
    return this;
  }

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  @Override
  public BasicProperties addProperty(String key, String value) {
    this.properties.put(key, value);
    return this;
  }

  /**
   * Adds a property to the cache
   *
   * @param key the key that is used to save and request the data
   * @param value the value that should be saved
   * @return the current instance
   */
  @Override
  public BasicProperties addProperty(String key, long value) {
    this.properties.put(key, value);
    return this;
  }

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  @Override
  public double getDouble(String key) {
    return (double) this.properties.get(key);
  }

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  @Override
  public int getInt(String key) {
    return (int) this.properties.get(key);
  }

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  @Override
  public Object getObject(String key) {
    return this.properties.get(key);
  }

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  @Override
  public String getString(String key) {
    return (String) this.properties.get(key);
  }

  /**
   * Returns a property from the cache
   *
   * @param key the key that is used to save and request the data
   * @return the cached data
   */
  @Override
  public long getLong(String key) {
    return (long) this.properties.get(key);
  }

  /**
   * Creates a new instance with the same data
   *
   * @return a new instance with the same data
   */
  @Override
  protected Properties clone() {
    return new BasicProperties(new HashMap<>(this.properties));
  }

  /**
   * Returns if the cache contains a given key
   *
   * @param key the key that is used to save and request the data
   * @return true if the cache contains the given key, false if not
   */
  @Override
  public boolean has(String key) {
    return this.properties.containsKey(key);
  }

  /**
   * Returns if the cache contains a multiple given keys
   *
   * @param keys the keys that are used to save and request the data
   * @return true if the cache contains all of the given keys, false if not
   */
  @Override
  public boolean has(String... keys) {
    for (String key : keys) {
      if (!has(key)) {
        return false;
      }
    }
    return true;
  }
}
