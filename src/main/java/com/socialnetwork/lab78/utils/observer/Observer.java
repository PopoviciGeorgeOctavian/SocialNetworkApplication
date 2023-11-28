package com.socialnetwork.lab78.utils.observer;

public interface Observer<E extends Event> {
    void update(E t);
}
