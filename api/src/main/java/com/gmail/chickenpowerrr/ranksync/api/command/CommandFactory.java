package com.gmail.chickenpowerrr.ranksync.api.command;

/**
 * This interface manages all of the things that are needed to easily control commands on the given
 * service
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface CommandFactory {

  /**
   * Get a command based on its label
   *
   * @param label the label that represents the command
   * @return the command that matches the given label
   */
  Command getCommand(String label);

  /**
   * Adds a command to be able to execute it when it's called by someone
   *
   * @param command the command that should be added
   */
  void addCommand(Command command);
}
