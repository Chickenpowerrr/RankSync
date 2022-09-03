package com.gmail.chickenpowerrr.ranksync.discord.event;

import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerUpdateOnlineStatusEvent;
import com.gmail.chickenpowerrr.ranksync.api.player.Status;
import com.gmail.chickenpowerrr.ranksync.discord.bot.DiscordBot;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

/**
 * This class handles all of the useful Discord events, coming in through the JDA
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class DiscordEventListeners implements EventListener {

  private final DiscordBot bot;

  /**
   * @param bot the running Discord Bot
   */
  public DiscordEventListeners(Bot<Member, Role> bot) {
    this.bot = (DiscordBot) bot;
  }

  /**
   * Updates userdata when they change their Discord status, enables the bot when the ReadyEvent
   * comes in and handles the incoming Discord commands
   *
   * @param event the event that triggers the methods
   */
  @Override
  public void onEvent(@NotNull GenericEvent event) {
    if (event instanceof UserUpdateOnlineStatusEvent) {
      UserUpdateOnlineStatusEvent onlineStatusEvent = (UserUpdateOnlineStatusEvent) event;
      if (onlineStatusEvent.getGuild().equals(bot.getGuild())) {
        boolean fromOffline = onlineStatusEvent.getOldOnlineStatus().equals(OnlineStatus.OFFLINE);
        boolean toOffline = onlineStatusEvent.getNewOnlineStatus().equals(OnlineStatus.OFFLINE);
        this.bot.getPlayerFactory().getPlayer(onlineStatusEvent.getMember()).thenAccept(
            player -> RankSyncApi.getApi().execute(new PlayerUpdateOnlineStatusEvent(player,
                fromOffline ? Status.OFFLINE : Status.ONLINE,
                toOffline ? Status.OFFLINE : Status.ONLINE)));
      }
    } else if (event instanceof ReadyEvent) {
      this.bot.enable(event.getJDA());
    }
  }
}
