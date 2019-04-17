package com.gmail.chickenpowerrr.ranksync.api.data;

import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;

/**
 * This class contains the basic logic to use a file as a resource
 *
 * @param <F> the implementation of the file used to retrieve the data
 * @author Chickenpowerrr
 * @since 1.2.0
 */
public abstract class AbstractFileDatabase<F extends AutoCreatingFile> implements Database {

  protected final RankResource rankResource;
  protected final F players;

  /**
   * Reads the rank resource and caches the file with userdata
   *
   * @param properties the properties that contain the rank resource
   * @param players the file with userdata
   */
  public AbstractFileDatabase(Properties properties, F players) {
    if (properties.has("rank_resource")) {
      this.rankResource = (RankResource) properties.getObject("rank_resource");
    } else {
      throw new IllegalStateException("This resource needs a rank resource");
    }
    this.players = players;
  }
}
