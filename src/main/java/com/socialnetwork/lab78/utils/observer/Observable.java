package com.socialnetwork.lab78.utils.observer;

/**
 * The `Observable` interface represents an object that can be observed by other objects (observers).
 * It is part of the observer design pattern.
 * @param <E> The type of events that can be observed.
 */
public interface Observable<E extends Event> {

    /**
     * Adds an observer to the list of observers.
     * @param o The observer to be added.
     */
    void addObserver(Observer<E> o);

    /**
     * Removes an observer from the list of observers.
     * @param o The observer to be removed.
     */
    void removeObserver(Observer<E> o);

    /**
     * Notifies all registered observers about an event.
     * @param t The event to be notified to observers.
     */
    void notify(E t);
}
