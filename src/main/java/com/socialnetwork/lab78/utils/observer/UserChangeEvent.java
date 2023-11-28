package com.socialnetwork.lab78.utils.observer;

import com.socialnetwork.lab78.domain.User;

public class UserChangeEvent implements Event{
    private ChangeEventType type;

    private User oldUser;

    private User newUser;

    public UserChangeEvent(ChangeEventType type, User oldUser, User newUser) {
        this.type = type;
        this.oldUser = oldUser;
        this.newUser = newUser;
    }

    public UserChangeEvent(ChangeEventType type, User newUser) {
        this.type = type;
        this.newUser = newUser;
    }

    public ChangeEventType getType() {
        return type;
    }

    public User getOldUser() {
        return oldUser;
    }

    public User getNewUser() {
        return newUser;
    }
}
