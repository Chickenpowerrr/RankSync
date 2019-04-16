package com.gmail.chickenpowerrr.ranksync.api.event;

/**
 * This interface can be implemented by events that can be cancelled before the action actually
 * takes place
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface CancelableEvent extends Event {

  /**
   * Get if the event got cancelled or not
   *
   * @return true if the event got cancelled, false if it didn't
   */
  boolean cancelled();

  /**
   * Set if the event should get cancelled or not
   *
   * @param cancelled true if this event shouldn't happen, false if it should
   */
  void setCancelled(boolean cancelled);
}
