package com.gmail.chickenpowerrr.ranksync.api.link;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import java.util.List;

/**
 * This interface contains all the requirements for a link
 *
 * @author Chickenpowerrr
 * @since 1.4.0
 */
public interface Link {

  /**
   * Returns all Minecraft ranks involved in this link
   */
  List<String> getMinecraftRanks();

  /**
   * Returns all platform ranks involved in this link
   */
  List<String> getPlatformRanks();

  /**
   * Returns the format used for this link
   */
  String getNameFormat();

  /**
   * Returns the bot which has to sync the rank
   */
  Bot<?, ?> getBot();
}
