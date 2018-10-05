package com.gmail.chickenpowerrr.ranksync.api.event;

public interface CancelableEvent extends Event {

    boolean cancelled();

    void setCancelled(boolean cancelled);
}
