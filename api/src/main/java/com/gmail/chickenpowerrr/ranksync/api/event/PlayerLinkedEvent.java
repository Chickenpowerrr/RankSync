package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event will get called when a player synchronizes its account
 *
 * @author Chickenpowerrr
 * @since 1.2.0
 */
@Getter
@AllArgsConstructor
public class PlayerLinkedEvent implements Event {

  private final Player player;
}
