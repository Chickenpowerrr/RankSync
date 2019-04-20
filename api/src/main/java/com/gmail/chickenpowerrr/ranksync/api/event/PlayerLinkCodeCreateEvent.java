package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event will get called when a player created a code to link its account
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public class PlayerLinkCodeCreateEvent implements PlayerEvent {

  private final Player player;
  private final String code;
}
