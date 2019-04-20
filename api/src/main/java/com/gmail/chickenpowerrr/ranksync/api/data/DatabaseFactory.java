package com.gmail.chickenpowerrr.ranksync.api.data;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;

/**
 * This interface manages all of the possible databases
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface DatabaseFactory {

  /**
   * Returns a new database if the requested database doesn't exist, otherwise it will give the cached
   * database instance
   *
   * @param name the name of the database
   * @param credentials the information that is needed to establish a connection with the database
   * @return a new database if the requested database doesn't exist, otherwise it will give the
   * cached database instance
   */
  Database getDatabase(String name, Properties credentials);

  /**
   * Returns the {@code Bot} that uses this factory
   *
   * @return the {@code Bot} that uses this factory
   */
  Bot getBot();
}
