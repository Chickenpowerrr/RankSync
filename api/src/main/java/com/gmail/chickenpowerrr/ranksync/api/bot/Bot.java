package com.gmail.chickenpowerrr.ranksync.api.bot;

import com.gmail.chickenpowerrr.languagehelper.LanguageHelper;
import com.gmail.chickenpowerrr.ranksync.api.command.CommandFactory;
import com.gmail.chickenpowerrr.ranksync.api.data.Database;
import com.gmail.chickenpowerrr.ranksync.api.data.DatabaseFactory;
import com.gmail.chickenpowerrr.ranksync.api.name.NameResource;
import com.gmail.chickenpowerrr.ranksync.api.player.PlayerFactory;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankFactory;
import java.util.Collection;

/**
 * This interface is primary used to control all of the factories that can be used to get the data
 * that a developer need to control users and synchronize their ranks between multiple platforms
 *
 * @param <P> the Player class used by the platform API, not the RankSync API
 * @param <R> the Rank class used by the platform API, not the RankSync API
 */
public interface Bot<P, R> {

  /**
   * Shuts down the bot.
   */
  void shutdown();

  /**
   * Updates the way the Bot should get its data
   *
   * @param database the new way the Bot should get its data
   */
  void setEffectiveDatabase(Database database);

  /**
   * Returns the way the Bot requests its data
   */
  Database getEffectiveDatabase();

  /**
   * Returns the factory that controls all of the Player related data
   */
  PlayerFactory<P> getPlayerFactory();

  /**
   * Returns the factory that controls all of the Rank related data
   */
  RankFactory<R> getRankFactory();

  /**
   * Returns the factory that controls all of the Database related data
   */
  DatabaseFactory getDatabaseFactory();

  /**
   * Returns the factory that controls all of the Command related data
   */
  CommandFactory getCommandFactory();

  /**
   * Returns the resource that controls the names of users
   */
  NameResource getNameResource();

  /**
   * Returns the platform that is assigned to this Bot
   */
  String getPlatform();

  /**
   * Updates the {@code LanguageHelper} that will be used to get the right translations
   *
   * @param languageHelper the {@code LanguageHelper} that will be used to get the right
   * translations
   */
  void setLanguageHelper(LanguageHelper languageHelper);

  /**
   * Updates the Language that will be used to get the right translations
   *
   * @param string the Language that will be used to get the right translations
   */
  void setLanguage(String string);

  /**
   * Returns all of the ranks the database contains
   */
  Collection<String> getAvailableRanks();

  /**
   * Returns if the ranks are case sensitive when they are requested by their name
   */
  boolean hasCaseSensitiveRanks();

  /**
   * Returns if the Bot has been enabled
   */
  boolean isEnabled();

  /**
   * Returns if the synchronized ranks should be removed from non synced players
   */
  boolean doesUpdateNonSynced();

  /**
   * Returns if the program should synchronize the usernames
   */
  boolean doesUpdateNames();

  /**
   * Returns the format for in which an username should be updated
   * if name sync has been enabled
   *
   * @return the format for in which an username should be updated
   * if name sync has been enabled
   */
  String getNameSyncFormat();

  /**
   * Returns the time in seconds a message sent to a public channel should stay there before
   * it needs to be deleted, -1 will make sure the message doesn't get deleted
   */
  int getDeleteTimer();

  /**
   * Returns the interval between times in which all users should be checked if their rank
   * is still correct, should be greater than 0 if you want to use this feature
   */
  int getUpdateInterval();
}
