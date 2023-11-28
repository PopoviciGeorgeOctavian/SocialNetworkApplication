package com.socialnetwork.lab78.domain;

import java.time.LocalDateTime;
import java.util.*;

public class FriendShip extends Entity<UUID>{

    private User user1;
    private User user2;
    private LocalDateTime date;

    private FriendRequest acceptance;
    public FriendShip(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(UUID.randomUUID());
        this.date=LocalDateTime.now();
        this.acceptance = FriendRequest.PENDING;
    }

    public FriendShip(UUID id, User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(id);
        this.date=LocalDateTime.now();
        this.acceptance = FriendRequest.PENDING;
    }

    public FriendShip(User user1, User user2, FriendRequest acceptance) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(UUID.randomUUID());
        this.date = LocalDateTime.now();
        this.acceptance = acceptance;
    }

    public FriendShip(UUID id, User user1, User user2, FriendRequest acceptance) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(id);
        this.date = LocalDateTime.now();
        this.acceptance = acceptance;
    }
    /**
     *
     * @return the date when the friendship was created
     */
    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FriendRequest getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(FriendRequest acceptance) {
        this.acceptance = acceptance;
    }

    @Override
    public String toString() {
        return "FriendShip {" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", id=" + id +
                ", request=" + acceptance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendShip))
            return false;
        FriendShip that = (FriendShip) o;
        return id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser1(), getUser2());
    }
}
