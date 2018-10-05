package com.gmail.chickenpowerrr.ranksync.api;

public interface Bot<P, R> {

    void setEffectiveDatabase(Database database);

    Database getEffectiveDatabase();

    PlayerFactory<P> getPlayerFactory();

    RankFactory<R> getRankFactory();

    DatabaseFactory getDatabaseFactory();

    CommandFactory getCommandFactory();

    String getName();
}
