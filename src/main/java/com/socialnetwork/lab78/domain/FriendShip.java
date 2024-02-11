package com.socialnetwork.lab78.domain;

import java.time.LocalDateTime;
import java.util.*;

/**
 * The FriendShip class represents a friendship between two users in the social network.
 * It includes information such as the users involved, the date of creation, and the acceptance status.
 */
public class FriendShip extends Entity<UUID> {

    private User user1;
    private User user2;
    private LocalDateTime date;
    private FriendRequest acceptance;

    /**
     * Constructor for creating a friendship with default values, setting acceptance to PENDING.
     *
     * @param user1 The first user in the friendship.
     * @param user2 The second user in the friendship.
     */
    public FriendShip(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(UUID.randomUUID());
        this.date = LocalDateTime.now();
        this.acceptance = FriendRequest.PENDING;
    }

    /**
     * Constructor for creating a friendship with a specified ID and default acceptance status (PENDING).
     *
     * @param id    The unique identifier for the friendship.
     * @param user1 The first user in the friendship.
     * @param user2 The second user in the friendship.
     */
    public FriendShip(UUID id, User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(id);
        this.date = LocalDateTime.now();
        this.acceptance = FriendRequest.PENDING;
    }

    /**
     * Constructor for creating a friendship with a specified acceptance status.
     *
     * @param user1      The first user in the friendship.
     * @param user2      The second user in the friendship.
     * @param acceptance The acceptance status of the friendship.
     */
    public FriendShip(User user1, User user2, FriendRequest acceptance) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(UUID.randomUUID());
        this.date = LocalDateTime.now();
        this.acceptance = acceptance;
    }

    /**
     * Constructor for creating a friendship with a specified ID and acceptance status.
     *
     * @param id         The unique identifier for the friendship.
     * @param user1      The first user in the friendship.
     * @param user2      The second user in the friendship.
     * @param acceptance The acceptance status of the friendship.
     */
    public FriendShip(UUID id, User user1, User user2, FriendRequest acceptance) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(id);
        this.date = LocalDateTime.now();
        this.acceptance = acceptance;
    }

    /**
     * Get the first user in the friendship.
     *
     * @return The first user in the friendship.
     */
    public User getUser1() {
        return user1;
    }

    /**
     * Set the first user in the friendship.
     *
     * @param user1 The first user in the friendship.
     */
    public void setUser1(User user1) {
        this.user1 = user1;
    }

    /**
     * Get the second user in the friendship.
     *
     * @return The second user in the friendship.
     */
    public User getUser2() {
        return user2;
    }

    /**
     * Set the second user in the friendship.
     *
     * @param user2 The second user in the friendship.
     */
    public void setUser2(User user2) {
        this.user2 = user2;
    }

    /**
     * Get the date when the friendship was created.
     *
     * @return The date when the friendship was created.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Set the date when the friendship was created.
     *
     * @param date The date when the friendship was created.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Get the acceptance status of the friendship.
     *
     * @return The acceptance status of the friendship.
     */
    public FriendRequest getAcceptance() {
        return acceptance;
    }

    /**
     * Set the acceptance status of the friendship.
     *
     * @param acceptance The acceptance status of the friendship.
     */
    public void setAcceptance(FriendRequest acceptance) {
        this.acceptance = acceptance;
    }

    /**
     * Override of the toString() method to provide a string representation of the Friendship object.
     *
     * @return A string representation of the Friendship object.
     */
    @Override
    public String toString() {
        return "FriendShip {" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", id=" + id +
                ", request=" + acceptance +
                '}';
    }

    /**
     * Override of the equals() method to compare Friendship objects based on their unique identifier.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendShip))
            return false;
        FriendShip that = (FriendShip) o;
        return id.equals(that.getId());
    }

    /**
     * Override of the hashCode() method to generate a hash code based on the users involved.
     *
     * @return The hash code for the Friendship object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUser1(), getUser2());
    }
}