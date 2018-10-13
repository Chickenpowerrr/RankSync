package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.Command;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.Status;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerUpdateOnlineStatusEvent;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DiscordEventListeners implements EventListener {

    private final DiscordBot bot;

    DiscordEventListeners(Bot<Member, Role> bot) {
        this.bot = (DiscordBot) bot;
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof UserUpdateOnlineStatusEvent) {
            UserUpdateOnlineStatusEvent onlineStatusEvent = (UserUpdateOnlineStatusEvent) event;
            if(onlineStatusEvent.getGuild().equals(bot.getGuild())) {
                boolean fromOffline = onlineStatusEvent.getOldOnlineStatus().equals(OnlineStatus.OFFLINE);
                boolean toOffline = onlineStatusEvent.getNewOnlineStatus().equals(OnlineStatus.OFFLINE);
                this.bot.getPlayerFactory().getPlayer(onlineStatusEvent.getMember()).thenAccept(player -> RankSyncApi.getApi().execute(new PlayerUpdateOnlineStatusEvent(player, fromOffline ? Status.OFFLINE : Status.ONLINE, toOffline ? Status.OFFLINE : Status.ONLINE)));
            }
        } else if(event instanceof ReadyEvent) {
            this.bot.enable(event.getJDA());
        } else if(event instanceof MessageReceivedEvent) {
            MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
            if(messageReceivedEvent.getGuild() != null && messageReceivedEvent.getGuild().equals(bot.getGuild())) {
                if(messageReceivedEvent.getMessage().getContentStripped().startsWith("!")) {
                    List<String> commandData = Arrays.asList(messageReceivedEvent.getMessage().getContentStripped().split(" "));
                    String commandLabel = commandData.get(0).replaceFirst("!", "").toLowerCase();
                    Command command = this.bot.getCommandFactory().getCommand(commandLabel);
                    if(command != null) {
                        this.bot.getPlayerFactory().getPlayer(messageReceivedEvent.getMember()).thenAccept(player -> {
                            String message = command.execute(player, commandData.size() > 0 ? commandData.subList(1, commandData.size()) : new ArrayList<>());
                            if(message != null) {
                                messageReceivedEvent.getTextChannel().sendMessage(message).queue();
                            }
                        });
                    }
                }
            }
        }
    }
}
