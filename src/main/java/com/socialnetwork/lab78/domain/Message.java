package com.socialnetwork.lab78.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Message extends Entity<UUID> {
    private User from;
    private List<User> to;
    private String messageText;
    private LocalDateTime date;

    public Message(User from, List<User> to, String messageText) {
        this.from = from;
        this.to = to;
        this.messageText = messageText;
        this.date = LocalDateTime.now();
        this.setId(UUID.randomUUID());
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
