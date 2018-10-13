package com.gmail.chickenpowerrr.ranksync.api;

/**
 * This interface is primary used to control all of
 * the factories that can be used to get the data that
 * a developer need to control users and synchronize their
 * ranks between multiple platforms
 *
 * @param <P> the Player class used by the platform API, not
 *            the RankSync API
 * @param <R> the Rank class used by the platform API, not
 *            the RankSync API
 */
public interface Bot<P, R> {

    /**
     * Update the way the Bot should get its data
     *
     * @param database the new way the Bot should get its data
     */
    void setEffectiveDatabase(Database database);

    /**
     * Get the way the Bot requests its data
     *
     * @return the way the Bot requests its data
     */
    Database getEffectiveDatabase();

    /**
     * Get the factory that controls all of the Player related data
     *
     * @return the factory that controls all of the Player related data
     */
    PlayerFactory<P> getPlayerFactory();

    /**
     * Get the factory that controls all of the Rank related data
     *
     * @return the factory that controls all of the Rank related data
     */
    RankFactory<R> getRankFactory();

    /**
     * Get the factory that controls all of the Database related data
     *
     * @return the factory that controls all of the Database related data
     */
    DatabaseFactory getDatabaseFactory();

    /**
     * Get the factory that controls all of the Command related data
     *
     * @return the factory that controls all of the Command related data
     */
    CommandFactory getCommandFactory();

    /**
     * Get the name that is assigned to this Bot
     *
     * @return the name that is assigned to this Bot
     */
    String getName();
}
