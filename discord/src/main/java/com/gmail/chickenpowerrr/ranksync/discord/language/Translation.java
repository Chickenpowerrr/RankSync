package com.gmail.chickenpowerrr.ranksync.discord.language;

import com.gmail.chickenpowerrr.languagehelper.LanguageHelper;
import lombok.Setter;

/**
 * This enum contains the translations that can get used by the Discord Bot
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public enum Translation {

  LINK_COMMAND_DESCRIPTION,
  LINK_COMMAND_ENABLE_PRIVATE_MESSAGES,
  LINK_COMMAND_ALREADY_LINKED,
  LINK_COMMAND_RIGHTTHERE,
  LINK_COMMAND_REQUEST_LIMIT,
  LINK_COMMAND_PUBLIC,
  LINK_COMMAND_PRIVATE,
  DATABASE_VERSION_UNKNOWN;

  private static final String DEFAULT_LANGUAGE = "english";
  @Setter private static LanguageHelper languageHelper;
  @Setter private static String language;

  private final String key;

  Translation() {
    this.key = "discord-" + super.toString().toLowerCase().replace("_", "-");
  }

  Translation(String key) {
    this.key = key;
  }

  /**
   * Returns a translation based on the placeholders
   *
   * @param replacements the placeholders (placeholder, replacement, placeholder, replacement, etc.)
   * @return a translation based on the placeholders
   */
  public String getTranslation(String... replacements) {
    String message = languageHelper.getMessage(language, DEFAULT_LANGUAGE, this.key);

    for (int i = 0; i < replacements.length; i += 2) {
      message = message.replaceAll("%" + replacements[i] + "%", replacements[i + 1]);
    }
    return message.replace("!n", "\n");
  }

  @Override
  public String toString() {
    return this.key;
  }
}
