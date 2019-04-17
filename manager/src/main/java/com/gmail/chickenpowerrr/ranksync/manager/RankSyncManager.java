package com.gmail.chickenpowerrr.ranksync.manager;

import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.discord.bot.BotFactory;
import lombok.Getter;

public class RankSyncManager {

  @Getter public static final RankSyncManager instance = new RankSyncManager();

  private boolean started = false;

  private RankSyncManager() {
  }

  public synchronized void setup() {
    if (!this.started) {
      RankSyncApi.getApi().addBotFactory("Discord", BotFactory.getInstance());
      this.started = true;
    } else {
      throw new IllegalStateException("The program has already been initialized!");
    }
  }
}
