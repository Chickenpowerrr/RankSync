package com.gmail.chickenpowerrr.ranksync.api.event;

/**
 * This interface contains all of the method that a Listener
 * needs in order to trigger when an event gets called
 *
 * @param <T> the event this Listener is listening to
 */
public interface Listener<T extends Event> {

    /**
     * Get the event this Listener is listening to
     *
     * @return the event this Listener is listening to
     */
    Class<T> getTarget();

    /**
     * Defines what will happen when the event gets called
     *
     * @param event the event that triggered the method
     */
    void onEvent(T event);
}
