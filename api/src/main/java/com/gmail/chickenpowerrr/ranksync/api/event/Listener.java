package com.gmail.chickenpowerrr.ranksync.api.event;

public interface Listener<T extends Event> {

    Class<T> getTarget();

    void onEvent(T event);
}
