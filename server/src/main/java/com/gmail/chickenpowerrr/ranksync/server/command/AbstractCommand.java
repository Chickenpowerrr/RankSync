package com.gmail.chickenpowerrr.ranksync.server.command;

import com.gmail.chickenpowerrr.ranksync.api.user.User;
import java.util.List;
import lombok.AllArgsConstructor;

/**
 * This class is a wrapper to allow servers to execute commands
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
@AllArgsConstructor
public abstract class AbstractCommand {

  private final String label;
  private final String description;
  private final String permission;
  private final List<String> aliases;

  /**
   * Executes the command
   *
   * @param user the executing player
   * @param label the label used when the command got executed
   * @param args the arguments given when executing the command
   */
  protected abstract void execute(User user, String label, String[] args);

  /**
   * Returns if the user is allowed to use this command
   *
   * @param user the executing user
   * @return if the user is allowed to use this command
   */
  protected abstract boolean isValidUser(User user);
}
