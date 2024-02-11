package com.socialnetwork.lab78.domain;

/**
 * The FriendRequest enum represents the different states of a friend request.
 * It includes ACCEPTED, REJECTED, and PENDING states.
 */
public enum FriendRequest {

    /**
     * Represents a friend request that has been accepted.
     */
    ACCEPTED,

    /**
     * Represents a friend request that has been rejected.
     */
    REJECTED,

    /**
     * Represents a friend request that is still pending, awaiting a response.
     */
    PENDING;
}