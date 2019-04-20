package com.gmail.chickenpowerrr.ranksync.discord.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import lombok.Getter;

/**
 * This class represents a Field in the Yaml to simplify loading a Yaml file
 *
 * @author Chickenpowerrr
 * @since 1.2.0
 */
public class YamlField {

  @Getter private final String key;
  private final Collection<Object> lastLayer;
  private final Collection<Map<String, Object>> nextLayer;

  /**
   * Loads all of the data the Field contains
   *
   * @param key the key used to access this Field
   * @param value the value of the Field
   */
  @SuppressWarnings("unchecked")
  YamlField(String key, Object value) {
    this.key = key;

    Collection<Object> lastLayer = new HashSet<>();
    Collection<Map<String, Object>> nextLayer = new HashSet<>();

    if (value instanceof Map) {
      nextLayer.add((Map<String, Object>) value);
    } else {
      lastLayer.add(value);
    }

    this.lastLayer = lastLayer.size() > 0 ? lastLayer : null;
    this.nextLayer = nextLayer.size() > 0 ? nextLayer : null;
  }

  /**
   * Returns the Objects that don't have sub-values
   */
  public Collection<Object> getLastLayer() {
    return this.lastLayer != null ? Collections.unmodifiableCollection(this.lastLayer) : null;
  }

  /**
   * Returns child Fields of this Field
   */
  public Collection<Map<String, Object>> getNextLayer() {
    return this.nextLayer != null ? Collections.unmodifiableCollection(this.nextLayer) : null;
  }
}
