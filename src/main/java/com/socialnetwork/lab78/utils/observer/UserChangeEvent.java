package com.socialnetwork.lab78.utils.observer;

import com.socialnetwork.lab78.domain.User;

/**
 * The `UserChangeEvent` class represents an event that notifies observers about changes in a user.
 * It contains information about the type of change, the old user state, and the new user state.
 */
public class UserChangeEvent implements Event {

    // Enum representing different types of user change events
    private ChangeEventType type;

    // The user state before the change
    private User oldUser;

    // The user state after the change
    private User newUser;

    /**
     * Constructs a `UserChangeEvent` with the specified parameters.
     *
     * @param type    The type of change event.
     * @param oldUser The user state before the change.
     * @param newUser The user state after the change.
     */
    public UserChangeEvent(ChangeEventType type, User oldUser, User newUser) {
        this.type = type;
        this.oldUser = oldUser;
        this.newUser = newUser;
    }

    /**
     * Constructs a `UserChangeEvent` with the specified parameters when the old user state is not available.
     *
     * @param type    The type of change event.
     * @param newUser The user state after the change.
     */
    public UserChangeEvent(ChangeEventType type, User newUser) {
        this.type = type;
        this.newUser = newUser;
    }

    /**
     * Gets the type of the user change event.
     *
     * @return The type of change event.
     */
    public ChangeEventType getType() {
        return type;
    }

    /**
     * Gets the user state before the change.
     *
     * @return The user state before the change.
     */
    public User getOldUser() {
        return oldUser;
    }

    /**
     * Gets the user state after the change.
     *
     * @return The user state after the change.
     */
    public User getNewUser() {
        return newUser;
    }
}
