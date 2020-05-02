package com.gmail.chickenpowerrr.ranksync.core.config.exception;

import org.jetbrains.annotations.NotNull;

/**
 * This {@link Exception} might be thrown when a certain
 * key in a config didn't contain a value.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class UnknownKeyException extends RuntimeException {

  private static final long serialVersionUID = 5636212514234247765L;

  /**
   * Constructs a new {@link Exception} based on the path
   * which didn't contain a value.
   *
   * @param path the path which didn't contain a value
   */
  public UnknownKeyException(@NotNull String path) {
    super("No key could be found at: '" + path + "'");
  }
}
