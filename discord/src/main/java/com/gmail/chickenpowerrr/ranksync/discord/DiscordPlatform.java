package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public class DiscordPlatform extends Platform<DiscordPlatform> {

  public static final int MAX_NAME_SIZE = 32;
  public static final int MIN_NAME_SIZE = 2;
  public static final String[] ILLEGAL_NAME_CHARACTERS = new String[] {"@", "#", "```"};

  public DiscordPlatform(@NotNull String baseNameFormat) {
    super("Discord", baseNameFormat, true);
  }

  @Override
  public boolean isValidName(@NotNull String name) {
    return name.length() <= MAX_NAME_SIZE && name.length() >= MIN_NAME_SIZE
        && Arrays.stream(ILLEGAL_NAME_CHARACTERS).noneMatch(name::contains);
  }

  @Override
  public boolean isValidFormat(@NotNull String format) {
    return isValidName(format.replace("%name%", "a name"));
  }
}
