package com.gmail.chickenpowerrr.ranksync.discord.bot;

import com.gmail.chickenpowerrr.languagehelper.LanguageHelper;
import com.gmail.chickenpowerrr.ranksync.api.*;
import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.command.CommandFactory;
import com.gmail.chickenpowerrr.ranksync.api.data.Database;
import com.gmail.chickenpowerrr.ranksync.api.data.DatabaseFactory;
import com.gmail.chickenpowerrr.ranksync.api.player.PlayerFactory;
import com.gmail.chickenpowerrr.ranksync.api.data.Properties;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankFactory;
import com.gmail.chickenpowerrr.ranksync.api.event.BotEnabledEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.BotForceShutdownEvent;
import com.gmail.chickenpowerrr.ranksync.discord.event.DiscordEventListeners;
import com.gmail.chickenpowerrr.ranksync.discord.command.LinkCommand;
import com.gmail.chickenpowerrr.ranksync.discord.language.Translation;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import javax.security.auth.login.LoginException;
import java.util.HashSet;

public class DiscordBot implements Bot<Member, Role> {

  @Getter private Guild guild;
  @Getter private final String platform = "Discord";
  @Getter @Setter private Database effectiveDatabase;
  @Getter private PlayerFactory<Member> playerFactory;
  @Getter private RankFactory<Role> rankFactory;
  @Getter private DatabaseFactory databaseFactory;
  @Getter private CommandFactory commandFactory;

  @Getter @Setter private boolean enabled;

  private final Properties properties;

  private JDA jda;

  DiscordBot(Properties properties) {
    this.enabled = false;
    this.properties = properties;
    Translation.setLanguageHelper((LanguageHelper) properties.getObject("language_helper"));
    Translation.setLanguage(properties.getString("language"));

    try {
      new JDABuilder(properties.getString("token"))
          .addEventListener(new DiscordEventListeners(this)).build();
    } catch (LoginException e) {
      throw new RuntimeException(e);
    }
  }

  public void enable(JDA jda) {
    this.jda = jda;
    this.guild = jda.getGuildById(this.properties.getLong("guild_id"));
    if (this.guild != null) {
      this.rankFactory = com.gmail.chickenpowerrr.ranksync.discord.rank.RankFactory
          .getInstance(this, guild);
      this.playerFactory = com.gmail.chickenpowerrr.ranksync.discord.player.PlayerFactory
          .getInstance(this, guild);
      this.databaseFactory = com.gmail.chickenpowerrr.ranksync.discord.data.DatabaseFactory
          .getInstance(this, guild);
      this.commandFactory = com.gmail.chickenpowerrr.ranksync.discord.command.CommandFactory
          .getInstance(this, guild);
      this.effectiveDatabase = this.databaseFactory
          .getDatabase(this.properties.getString("type"), this.properties);
      this.commandFactory.addCommand(new LinkCommand("link", new HashSet<>()));
      RankSyncApi.getApi().execute(new BotEnabledEvent(this));
      this.enabled = true;
    } else {
      RankSyncApi.getApi()
          .execute(new BotForceShutdownEvent(this, "The given guild id is invalid"));
      jda.shutdownNow();
    }
  }

  @Override
  public void setLanguageHelper(LanguageHelper languageHelper) {
    Translation.setLanguageHelper(languageHelper);
  }

  @Override
  public void setLanguage(String string) {
    Translation.setLanguage(string);
  }

  @Override
  public Collection<String> getAvailableRanks() {
    return this.jda.getGuilds().stream().map(Guild::getRoles).flatMap(Collection::stream)
        .map(Role::getName).collect(Collectors.toSet());
  }

  @Override
  public boolean hasCaseSensitiveRanks() {
    return false;
  }
}
