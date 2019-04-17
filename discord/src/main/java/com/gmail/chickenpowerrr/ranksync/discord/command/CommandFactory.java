package com.gmail.chickenpowerrr.ranksync.discord.command;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.command.Command;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;

/**
 * This interface manages all of the things that are needed to easily control the Discord commands
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class CommandFactory implements
    com.gmail.chickenpowerrr.ranksync.api.command.CommandFactory {

  private static final Map<Guild, CommandFactory> instances = new HashMap<>();

  private final Bot bot;
  private final Guild guild;
  private final Map<String, Command> commands = new HashMap<>();

  /**
   * @param bot the Bot that is running
   * @param guild the Guild the Bot is running on
   */
  private CommandFactory(Bot bot, Guild guild) {
    this.bot = bot;
    this.guild = guild;
  }

  /**
   * Lazily returns an instance based on the Guild the Bot is running on
   *
   * @param bot the Bot that is running
   * @param guild the Guild the Bot is running on
   * @return an instance based on the Guild the Bot is running on
   */
  public static CommandFactory getInstance(Bot bot, Guild guild) {
    if (!instances.containsKey(guild)) {
      instances.put(guild, new CommandFactory(bot, guild));
    }
    return instances.get(guild);
  }

  @Override
  public Command getCommand(String label) {
    return this.commands.get(label.toLowerCase());
  }

  @Override
  public void addCommand(Command command) {
    this.commands.put(command.getLabel().toLowerCase(), command);
  }
}
