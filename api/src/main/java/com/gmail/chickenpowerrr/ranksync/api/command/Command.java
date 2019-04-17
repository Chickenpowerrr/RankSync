package com.gmail.chickenpowerrr.ranksync.api.command;

import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import java.util.Collection;
import java.util.List;

/**
 * This interface contains all of the method needed to create a functional command that will get
 * accepted by the {@code CommandFactory}
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface Command {

  /**
   * Returns the 'name' of the command
   */
  String getLabel();

  /**
   * Returns all aliases that, besides the name can get used to invoke this command
   */
  Collection<String> getAliases();

  /**
   * Checks if the given player is allowed to execute this command
   *
   * @param player the player who wants to execute this command
   * @return true if the player is allowed to execute this command, false if he isn't allowed to do
   * so
   */
  boolean hasPermission(Player player);

  /**
   * Executes the command for a specific player with the given arguments
   *
   * @param invoker the player that invokes the command
   * @param arguments the arguments that came with the execution of the command
   * @return the message that will be send into the channel where the command was executed
   */
  String execute(Player invoker, List<String> arguments);
}
