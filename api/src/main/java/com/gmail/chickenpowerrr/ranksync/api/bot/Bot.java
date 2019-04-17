package com.gmail.chickenpowerrr.ranksync.api.bot;

import com.gmail.chickenpowerrr.languagehelper.LanguageHelper;
import com.gmail.chickenpowerrr.ranksync.api.player.PlayerFactory;
import com.gmail.chickenpowerrr.ranksync.api.command.CommandFactory;
import com.gmail.chickenpowerrr.ranksync.api.data.Database;
import com.gmail.chickenpowerrr.ranksync.api.data.DatabaseFactory;
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
   * Update the way the Bot should get its data
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
   * Returns the platform that is assigned to this Bot
   */
  String getPlatform();

  /**
   * Update the {@code LanguageHelper} that will be used to get the right translations
   *
   * @param languageHelper the {@code LanguageHelper} that will be used to get the right
   * translations
   */
  void setLanguageHelper(LanguageHelper languageHelper);

  /**
   * Update the Language that will be used to get the right translations
   *
   * @param string the Language that will be used to get the right translations
   */
  void setLanguage(String string);

  Collection<String> getAvailableRanks();

  boolean hasCaseSensitiveRanks();
}
