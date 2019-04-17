package com.gmail.chickenpowerrr.ranksync.discord.language;

import com.gmail.chickenpowerrr.languagehelper.LanguageHelper;
import lombok.Setter;

public enum Translation {

  LINK_COMMAND_ALREADY_LINKED,
  LINK_COMMAND_RIGHTTHERE,
  LINK_COMMAND_REQUEST_LIMIT,
  LINK_COMMAND_PUBLIC,
  LINK_COMMAND_PRIVATE;

  @Setter private static LanguageHelper languageHelper;
  @Setter private static String language;

  private final String key;

  Translation() {
    this.key = "discord-" + super.toString().toLowerCase().replace("_", "-");
  }

  Translation(String key) {
    this.key = key;
  }

  public String getTranslation(String... replacements) {
    String message = languageHelper.getMessage(language, this.key);

    for (int i = 0; i < replacements.length; i += 2) {
      message = message.replaceAll("%" + replacements[i] + "%", replacements[i + 1]);
    }
    return message.replace("!n", "\n");
  }

  @Override
  public String toString() {
    return key;
  }
}
