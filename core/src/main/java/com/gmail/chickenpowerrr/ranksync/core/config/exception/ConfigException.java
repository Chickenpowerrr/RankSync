package com.gmail.chickenpowerrr.ranksync.core.config.exception;

import com.gmail.chickenpowerrr.ranksync.core.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * This is the general type of {@link Exception} which might be
 * thrown by a {@link Config} when something is invalid.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class ConfigException extends RuntimeException {

  private static final long serialVersionUID = 808945500728824528L;

  /**
   * Constructs a new {@link Exception} with the given message
   * as the reason why it has been thrown.
   *
   * @param message the reason why this {@link Exception} is thrown
   */
  public ConfigException(@NotNull String message) {
    super(message);
  }
}
