package com.gmail.chickenpowerrr.ranksync.core.config.exception;

import com.gmail.chickenpowerrr.ranksync.core.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * This {@link Exception} might be thrown when a certain
 * value in a {@link Config} isn't formatted properly.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class InvalidValueException extends ConfigException {

  private static final long serialVersionUID = -3621420882167628903L;

  /**
   * Constructs a new {@link Exception} based on the path
   * which didn't contain a properly formatted value.
   *
   * @param path the path which didn't contain a value
   */
  public InvalidValueException(@NotNull String path, @NotNull Class<?> type) {
    super("No value of type: '" + type.getSimpleName() + "' could be found at '" + path + "'");
  }
}
