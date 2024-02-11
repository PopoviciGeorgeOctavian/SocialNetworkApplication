package com.socialnetwork.lab78.utils.observer;

/**
 * The `Observer` interface represents an object that observes changes in an observable object.
 * It is part of the observer design pattern.
 *
 * @param <E> The type of events that can be observed.
 */
public interface Observer<E extends Event> {

    /**
     * Updates the observer with new information about a specific event.
     *
     * @param t The event that triggered the update.
     */
    void update(E t);
}