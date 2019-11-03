package com.gmail.chickenpowerrr.ranksync.discord.bot;

import com.gmail.chickenpowerrr.languagehelper.LanguageHelper;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.command.CommandFactory;
import com.gmail.chickenpowerrr.ranksync.api.data.Database;
import com.gmail.chickenpowerrr.ranksync.api.data.DatabaseFactory;
import com.gmail.chickenpowerrr.ranksync.api.data.Properties;
import com.gmail.chickenpowerrr.ranksync.api.event.BotEnabledEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.BotForceShutdownEvent;
import com.gmail.chickenpowerrr.ranksync.api.name.NameResource;
import com.gmail.chickenpowerrr.ranksync.api.player.PlayerFactory;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankFactory;
import com.gmail.chickenpowerrr.ranksync.discord.command.LinkCommand;
import com.gmail.chickenpowerrr.ranksync.discord.event.DiscordEventListeners;
import com.gmail.chickenpowerrr.ranksync.discord.language.Translation;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import javax.security.auth.login.LoginException;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class DiscordBot implements Bot<Member, Role> {

  @Getter
  private Guild guild;
  @Getter
  private final String platform = "Discord";
  @Getter
  @Setter
  private Database effectiveDatabase;
  @Getter
  private PlayerFactory<Member> playerFactory;
  @Getter
  private RankFactory<Role> rankFactory;
  @Getter
  private DatabaseFactory databaseFactory;
  @Getter
  private CommandFactory commandFactory;
  @Getter
  private NameResource nameResource;

  @Getter
  @Setter
  private boolean enabled;

  private Properties properties;

  private JDA jda;

  DiscordBot(Properties properties) {
    this.enabled = false;
    this.properties = properties;
    Translation.setLanguageHelper((LanguageHelper) properties.getObject("language_helper"));
    Translation.setLanguage(properties.getString("language"));
    this.nameResource = (NameResource) properties.getObject("name_resource");

    try {
      new JDABuilder(properties.getString("token"))
          .addEventListeners(new DiscordEventListeners(this)).build();
    } catch (LoginException e) {
      if (e.toString().contains("The provided token is invalid!")) {
        RankSyncApi.getApi()
            .execute(new BotForceShutdownEvent(this, "===================================",
                "RankSync Error:", "The Discord token provided in the config.yml is invalid.",
                "For more information see:",
                "https://github.com/Chickenpowerrr/RankSync/wiki/Getting-a-Discord-Token",
                "==================================="));
      } else {
        throw new RuntimeException(e);
      }
    }
  }

  public void enable(JDA jda) {
    this.jda = jda;
    this.guild = jda.getGuildById(this.properties.getLong("guild_id"));
    if (this.guild != null) {
      this.rankFactory = com.gmail.chickenpowerrr.ranksync.discord.rank.RankFactory
          .getInstance(this, guild);
      this.rankFactory
          .setShouldThrowPermissionWarnings(this.properties.getBoolean("permission_warnings"));
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
          .execute(new BotForceShutdownEvent(this, "===================================",
              "RankSync Error:", "The Guild ID provided in the config.yml is invalid.",
              "For more information see:",
              "https://github.com/Chickenpowerrr/RankSync/wiki/Getting-a-Discord-Guild-id",
              "==================================="));
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
    return getGuild().getRoles().stream().map(Role::getName).collect(Collectors.toSet());
  }

  @Override
  public boolean hasCaseSensitiveRanks() {
    return false;
  }

  @Override
  public boolean doesUpdateNonSynced() {
    return this.properties.getBoolean("update_non_synced");
  }

  @Override
  public boolean doesUpdateNames() {
    return this.properties.getBoolean("sync_names");
  }

  @Override
  public String getNameSyncFormat() {
    return this.properties.getString("name_format");
  }

  @Override
  public int getDeleteTimer() {
    return this.properties.getInt("delete_timer");
  }
}
