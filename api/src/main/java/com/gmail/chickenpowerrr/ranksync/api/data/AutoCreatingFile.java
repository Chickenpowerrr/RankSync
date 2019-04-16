package com.gmail.chickenpowerrr.ranksync.api.data;

import java.io.File;
import java.io.IOException;

public class AutoCreatingFile extends File {

  public AutoCreatingFile(String basePath, String fileName) {
    super(basePath, fileName);

    try {
      getParentFile().mkdirs();
      createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void save() {

  }
}
