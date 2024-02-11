package com.socialnetwork.lab78.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a message in a social network application.
 */
public class Message extends Entity<UUID> {
    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime data;
    private Message reply;

    /**
     * Constructs a new message with the specified sender, recipients, message content, and timestamp.
     *
     * @param from    The user who sent the message.
     * @param to      The list of users who are the recipients of the message.
     * @param message The content of the message.
     * @param data    The timestamp of the message.
     */
    public Message(User from, List<User> to, String message, LocalDateTime data) {
        this.setId(UUID.randomUUID());
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.reply = null;
    }

    /**
     * Constructs a new message with the specified ID, sender, recipients, message content, and timestamp.
     *
     * @param id      The unique identifier of the message.
     * @param from    The user who sent the message.
     * @param to      The list of users who are the recipients of the message.
     * @param message The content of the message.
     * @param data    The timestamp of the message.
     */
    public Message(UUID id, User from, List<User> to, String message, LocalDateTime data) {
        this.setId(id);
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.reply = null;
    }

    /**
     * Gets the user who sent the message.
     *
     * @return The user who sent the message.
     */
    public User getFrom() {
        return from;
    }

    /**
     * Sets the user who sent the message.
     *
     * @param from The user who sent the message.
     */
    public void setFrom(User from) {
        this.from = from;
    }

    /**
     * Gets the list of users who are recipients of the message.
     *
     * @return The list of users who are recipients of the message.
     */
    public List<User> getTo() {
        return to;
    }

    /**
     * Sets the list of users who are recipients of the message.
     *
     * @param to The list of users who are recipients of the message.
     */
    public void setTo(List<User> to) {
        this.to = to;
    }

    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the content of the message.
     *
     * @param message The content of the message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp of the message.
     *
     * @return The timestamp of the message.
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     * Sets the timestamp of the message.
     *
     * @param data The timestamp of the message.
     */
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    /**
     * Gets the reply message associated with this message.
     *
     * @return The reply message.
     */
    public Message getReply() {
        return reply;
    }

    /**
     * Sets the reply message associated with this message.
     *
     * @param reply The reply message.
     */
    public void setReply(Message reply) {
        this.reply = reply;
    }

    /**
     * Checks if the message is received from a specific user.
     *
     * @param user The user to check if the message is received from.
     * @return True if the message is received from the specified user, false otherwise.
     */
    public boolean isReceivedFrom(User user) {
        return this.getTo().contains(user) && this.getFrom().equals(user);
    }

    /**
     * Checks if this message is equal to another object.
     *
     * @param o The object to compare with this message.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message1 = (Message) o;
        return Objects.equals(from, message1.from) && Objects.equals(to, message1.to) && Objects.equals(message, message1.message) && Objects.equals(data, message1.data) && Objects.equals(reply, message1.reply);
    }

    /**
     * Generates a hash code for this message.
     *
     * @return The hash code for this message.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to, message, data, reply);
    }

    /**
     * Generates a string representation of the message.
     *
     * @return A string representation of the message.
     */
    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", reply=" + reply +
                ", id=" + id +
                '}';
    }
}