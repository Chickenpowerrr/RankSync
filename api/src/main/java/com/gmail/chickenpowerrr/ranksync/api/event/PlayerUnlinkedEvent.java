package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event will get called when a player unsynchronizes its account
 *
 * @author Chickenpowerrr
 * @since 1.2.0
 */
@Getter
@AllArgsConstructor
public class PlayerUnlinkedEvent implements Event {

  private final Player player;
}
