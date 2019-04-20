package com.gmail.chickenpowerrr.ranksync.discord.data;

import com.gmail.chickenpowerrr.ranksync.api.data.AutoCreatingFile;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * This class is a wrapper to retrieve data from a Yaml file
 *
 * @author Chickenpowerrr
 * @since 1.2.0
 */
public class YamlFile extends AutoCreatingFile {

  private final Yaml yaml;
  private final Map<String, Object> values;

  /**
   * Sets the Yaml options to be pretty and load the values from the file
   *
   * @param basePath the path to the file
   * @param fileName the name of the file
   */
  public YamlFile(String basePath, String fileName) {
    super(basePath, fileName);

    DumperOptions options = new DumperOptions();
    // Pretty print for debugging purposes
    //    options.setIndent(2);
    //    options.setPrettyFlow(true);
    //    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    this.yaml = new Yaml(options);

    try (InputStream inputStream = new FileInputStream(this)) {
      this.values = getValues(this.yaml.load(inputStream));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Saves the Yaml with the updated data
   */
  @SuppressWarnings("unchecked")
  @Override
  public void save() {
    Map<String, Object> yaml = new HashMap<>();

    this.values.forEach((path, value) -> {
      try (FileWriter fileWriter = new FileWriter(this)) {

        List<String> stringPath = Arrays.asList(path.split("\\."));

        Map<String, Object> currentMap = yaml;
        for (int i = 0; i < stringPath.size(); i++) {
          if (i == stringPath.size() - 1) {
            currentMap.put(stringPath.get(i), value);
          } else {
            if (yaml.containsKey(stringPath.get(i))) {
              currentMap = (Map<String, Object>) yaml.get(stringPath.get(i));
            } else {
              Map<String, Object> newMap = new HashMap<>();
              currentMap.put(stringPath.get(i), newMap);
              currentMap = newMap;
            }
          }
        }
        this.yaml.dump(yaml, fileWriter);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  /**
   * Returns a value from the Yaml file
   *
   * @param path the path to the value
   * @param <T> the type of the value
   * @return the value at the given path
   */
  @SuppressWarnings("unchecked")
  public <T> T getValue(String path) {
    return (T) this.values.get(path);
  }

  /**
   * Sets a value at the given path
   *
   * @param path the path to the value
   * @param value the value that should get set
   * @param <T> the type of the value
   */
  public <T> void setValue(String path, T value) {
    this.values.put(path, value);
  }

  /**
   * Deletes a value from a given path
   *
   * @param path the path to the value
   */
  public void removeValue(String path) {
    this.values.remove(path);
  }

  /**
   * Retrieves the important data from the given Object and puts them into the given map
   *
   * @param key the prefix that should be used to get the keys later on
   * @param object the object that should be checked
   * @param objects the map that is going to contain all of the Objects the Yaml file contains
   */
  private void setValues(String key, Object object, Map<String, Object> objects) {
    YamlField field = new YamlField(key, object);

    if (field.getLastLayer() != null) {
      field.getLastLayer().forEach(o -> objects.put(field.getKey(), o));
    }

    if (field.getNextLayer() != null) {
      field.getNextLayer()
          .forEach(map -> map.forEach((s, o) -> setValues(key + "." + s, o, objects)));
    }
  }

  /**
   * Returns the values from the Yaml generated Map
   *
   * @param objects the Yaml generated Map
   * @return the new Map that contains the values without the other Maps inside of the Map
   */
  private Map<String, Object> getValues(Map<String, Object> objects) {
    Map<String, Object> formattedObjects = new HashMap<>();
    if (objects != null) {
      objects.forEach(((s, o) -> {
        Map<String, Object> partObjects = new HashMap<>();
        setValues(s, o, partObjects);
        formattedObjects.putAll(partObjects);
      }));
    }
    return formattedObjects;
  }
}
