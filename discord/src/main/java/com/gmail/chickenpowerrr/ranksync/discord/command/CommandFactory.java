package com.gmail.chickenpowerrr.ranksync.discord.command;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.command.Command;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory implements
    com.gmail.chickenpowerrr.ranksync.api.command.CommandFactory {

    private static final Map<Guild, CommandFactory> instances = new HashMap<>();

    private final Bot bot;
    private final Guild guild;
    private final Map<String, Command> commands = new HashMap<>();

    private CommandFactory(Bot bot, Guild guild) {
        this.bot = bot;
        this.guild = guild;
    }

    public static CommandFactory getInstance(Bot bot, Guild guild) {
        if(!instances.containsKey(guild)) {
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
