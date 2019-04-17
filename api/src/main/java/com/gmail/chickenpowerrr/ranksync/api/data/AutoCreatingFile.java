package com.gmail.chickenpowerrr.ranksync.api.data;

import java.io.File;
import java.io.IOException;

/**
 * This class is a file that gets automatically generated if it didn't exist already as soon as the
 * object has been created
 *
 * @author Chickenpowerrr
 * @since 1.2.0
 */
public class AutoCreatingFile extends File {

  /**
   * Creates the directories and the file the object represents
   *
   * @param basePath the path to the file
   * @param fileName the name of the file
   */
  public AutoCreatingFile(String basePath, String fileName) {
    super(basePath, fileName);

    try {
      getParentFile().mkdirs();
      createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves the file with the right data
   */
  public void save() {

  }
}
