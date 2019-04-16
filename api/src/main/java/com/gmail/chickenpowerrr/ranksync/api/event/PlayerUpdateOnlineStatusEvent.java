package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import com.gmail.chickenpowerrr.ranksync.api.player.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event will get called when a player changed its status
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public class PlayerUpdateOnlineStatusEvent implements PlayerEvent {

  private final Player player;
  private final Status from;
  private final Status to;
}
