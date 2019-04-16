package com.gmail.chickenpowerrr.ranksync.api;

public abstract class AbstractFileDatabase<F extends AutoCreatingFile> implements Database {

  protected final RankResource rankResource;

  protected final F players;

  public AbstractFileDatabase(Properties properties, F players) {
    if (properties.has("rank_resource")) {
      this.rankResource = (RankResource) properties.getObject("rank_resource");
    } else {
      throw new IllegalStateException("This resource needs a rank resource");
    }
    this.players = players;
  }
}
