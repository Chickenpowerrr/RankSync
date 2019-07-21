package com.gmail.chickenpowerrr.ranksync.server.listener;

import com.gmail.chickenpowerrr.ranksync.api.event.BotForceShutdownEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import lombok.AllArgsConstructor;

/**
 * This class shuts the plugin down whenever the Bot shuts down
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@AllArgsConstructor
public class BotForceShutdownEventListener implements Listener<BotForceShutdownEvent> {

  private final RankSyncServerPlugin rankSyncServerPlugin;

  @Override
  public Class<BotForceShutdownEvent> getTarget() {
    return BotForceShutdownEvent.class;
  }

  /**
   * Disables the plugin when the Bot shuts down
   *
   * @param event the event that triggered the method
   */
  @Override
  public void onEvent(BotForceShutdownEvent event) {
    this.rankSyncServerPlugin.shutdown(event.getReason());
  }
}
