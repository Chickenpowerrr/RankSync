package com.gmail.chickenpowerrr.ranksync.core.test.util;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import org.jetbrains.annotations.NotNull;

public class TestPlatform extends Platform<TestPlatform> {

  public TestPlatform(@NotNull String name, @NotNull String baseNameFormat, boolean canChangeName) {
    super(name, baseNameFormat, canChangeName);
  }

  public TestPlatform() {
    this("name", "%name%", true);
  }

  @Override
  public boolean isValidName(@NotNull String name) {
    return false;
  }

  @Override
  public boolean isValidFormat(@NotNull String format) {
    return false;
  }
}
