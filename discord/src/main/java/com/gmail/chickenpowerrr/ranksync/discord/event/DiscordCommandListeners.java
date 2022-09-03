package com.gmail.chickenpowerrr.ranksync.discord.event;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import com.gmail.chickenpowerrr.ranksync.discord.bot.DiscordBot;
import com.gmail.chickenpowerrr.ranksync.discord.command.LinkCommand;
import com.gmail.chickenpowerrr.ranksync.discord.language.Translation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public class DiscordCommandListeners extends ListenerAdapter {

  private final DiscordBot bot;
  private final LinkCommand command;

  public DiscordCommandListeners(Bot<Member, Role> bot) {
    this.bot = (DiscordBot) bot;
    this.command = new LinkCommand("link", new HashSet<>());
  }

  @Override
  public void onSlashCommand(@NotNull SlashCommandEvent event) {
    if (!event.getUser().isBot()
        && Objects.equals(event.getGuild(), this.bot.getGuild())) {
      this.bot.getPlayerFactory().getPlayer(event.getMember())
          .thenAccept(Player::update).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
          });

      this.bot.getPlayerFactory().getPlayer(event.getMember())
          .thenAccept(player -> {
            String message = this.command.execute(player, new ArrayList<>());
            if (message != null) {
              event.reply(message).setEphemeral(true).queue();
            }
          }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
          });
    }
  }

  @Override
  public void onGuildReady(@NotNull GuildReadyEvent event) {
    event.getGuild().updateCommands()
        .addCommands(new CommandData("link",
            Translation.LINK_COMMAND_DESCRIPTION.getTranslation()))
        .queue();
  }
}
