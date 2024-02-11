package com.socialnetwork.lab78.domain;


import java.util.*;

/**
 * Represents a user in a social network application.
 */
public class User extends Entity<UUID> {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Map<UUID, User> friends;

    /**
     * Constructs a new user with the specified first and last name.
     *
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new HashMap<>();
        this.setId(UUID.randomUUID());
    }

    /**
     * Constructs a new user with the specified ID, first name, and last name.
     *
     * @param id        The unique identifier of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     */
    public User(UUID id, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new HashMap<>();
        this.setId(id);
    }

    /**
     * Constructs a new user with the specified first name, last name, and email.
     *
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email address of the user.
     */
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.friends = new HashMap<>();
        this.setId(UUID.randomUUID());
    }

    /**
     * Constructs a new user with the specified ID, first name, last name, and email.
     *
     * @param id        The unique identifier of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email address of the user.
     */
    public User(UUID id, String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.friends = new HashMap<>();
        this.setId(id);
    }

    /**
     * Constructs a new user with the specified ID, first name, last name, email, and password.
     *
     * @param id        The unique identifier of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email address of the user.
     * @param password  The password of the user.
     */
    public User(UUID id, String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.friends = new HashMap<>();
        this.setId(id);
    }

    /**
     * Constructs a new user with the specified first name, last name, email, and password.
     * Generates a random unique ID for the user.
     *
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email address of the user.
     * @param password  The password of the user.
     */
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.friends = new HashMap<>();
        this.setId(UUID.randomUUID());
    }

    /**
     * Gets the first name of the user.
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The new first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The new last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the number of friends the user has.
     *
     * @return The number of friends.
     */
    public int getNumberOfFriends() {
        return this.friends.size();
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The new email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets a list of the user's friends.
     *
     * @return A list of user objects representing friends.
     */
    public List<User> getFriends() {
        return new ArrayList<>(this.friends.values());
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Generates a string representation of the user.
     *
     * @return A string representation of the user.
     */
    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * Checks if this user is equal to another object.
     *
     * @param o The object to compare with this user.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    /**
     * Generates a hash code for this user.
     *
     * @return The hash code for this user.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    /**
     * Adds a friend to the user's friend list.
     *
     * @param u The user to add as a friend.
     */
    public void addFriend(User u) {
        this.friends.put(u.getId(), u);
    }

    /**
     * Removes a friend from the user's friend list.
     *
     * @param u The user to remove from the friend list.
     * @return True if the friend was successfully removed, false otherwise.
     */
    public boolean removeFriend(User u) {
        return this.friends.remove(u.getId()) != null;
    }
}