package com.socialnetwork.lab78.domain;

import java.util.*;

public class User  extends Entity<UUID>{
    private String firstName;
    private String lastName;
    private Map<UUID, User> friends;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends=new HashMap<>();
        this.setId(UUID.randomUUID());


    }

    public User(UUID id, String firstName, String lastName) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new HashMap<>();
        this.setId(id);

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNumberOfFriends(){
        return this.friends.size();
    }

    public List<User> getFriends(){
        return new ArrayList<>(this.friends.values());
    }

    @Override
    public String toString() {
        return  firstName + '\'' +
                 lastName + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    public void addFriend(User u){
        this.friends.put(u.getId(), u);
    }

    public boolean removeFriend(User u){
        return this.friends.remove(u.getId()) != null;
    }


}