package com.socialnetwork.lab78.service;

import com.socialnetwork.lab78.Paging.PagingRepository;
import com.socialnetwork.lab78.controller.MessageAlert;
import com.socialnetwork.lab78.domain.Message;
import com.socialnetwork.lab78.domain.User;
import com.socialnetwork.lab78.repository.Repository;
import com.socialnetwork.lab78.repository.UserDBRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * MessageService provides business logic for handling Message entities in the social network application.
 */
public class MessageService {
    private final Repository<UUID, Message> messageRepo;

    private final Repository<UUID, User> userRepo;

    /**
     * Constructs a new MessageService with the specified repositories for messages and users.
     *
     * @param messageRepo The repository for messages.
     * @param userRepo    The repository for users.
     */
    public MessageService(Repository<UUID, Message> messageRepo, Repository<UUID, User> userRepo) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
    }

    /**
     * Adds a new message to the system.
     *
     * @param message The message to be added.
     * @return true if the message is added successfully, false if a message with the same ID already exists.
     */
    public boolean addMessage(Message message) {
        Optional<Message> existingMessage = messageRepo.save(message);

        if (existingMessage.isPresent()) {
            System.err.println("A message with this ID already exists!");
            return false;
        }

        return true;
    }

    /**
     * Deletes a message by its ID.
     *
     * @param id The ID of the message to be deleted.
     * @return The deleted message if found, otherwise null.
     */
    public Message deleteMessage(UUID id) {
        Optional<Message> deletedMessage = messageRepo.delete(id);

        if (deletedMessage.isPresent()) {
            return deletedMessage.get();
        } else {
            // Handle the case where the message with the given ID doesn't exist.
            return null;
        }
    }

    /**
     * Retrieves all messages and sorts them by date.
     *
     * @return A sorted list of all messages.
     */
    public List<Message> getAllMessages() {
        ArrayList<Message> messages = new ArrayList<>((Collection) messageRepo.findAll());

        messages.sort(Comparator.comparing(Message::getData));

        return messages;
    }

    /**
     * Adds a new message to the system between a sender and a list of recipients.
     *
     * @param from    The sender of the message.
     * @param to      The list of recipients.
     * @param message The content of the message.
     */
    public void addMessage(User from, List<User> to, String message) {
        // Create a new message
        Message newMessage = new Message(from, to, message, LocalDateTime.now());

        // Save the new message
        messageRepo.save(newMessage);

        // Set reply for specific conditions
        for (Message existingMessage : messageRepo.findAll()) {
            if (to.contains(existingMessage.getFrom()) &&
                    existingMessage.getTo().contains(from) &&
                    existingMessage.getReply() == null) {

                existingMessage.setReply(newMessage);
                messageRepo.update(existingMessage);
            }
        }

        System.out.println(newMessage);
    }

    /**
     * Retrieves the conversation messages between two users based on their IDs.
     *
     * @param id1 The ID of the first user.
     * @param id2 The ID of the second user.
     * @return A list of conversation messages sorted by date.
     * @throws IllegalArgumentException if either user is not found.
     */
    public List<Message> conversation(UUID id1, UUID id2) {
        User user1 = userRepo.findOne(id1).orElseThrow(() -> new IllegalArgumentException("User with id " + id1 + " not found"));
        User user2 = userRepo.findOne(id2).orElseThrow(() -> new IllegalArgumentException("User with id " + id2 + " not found"));

        return StreamSupport.stream(messageRepo.findAll().spliterator(), false)
                .filter(msg -> isConversationMessage(msg, user1, user2))
                .sorted(Comparator.comparing(Message::getData))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a message is part of a conversation between two users.
     *
     * @param message The message to check.
     * @param user1   The first user in the conversation.
     * @param user2   The second user in the conversation.
     * @return true if the message is part of the conversation, false otherwise.
     */
    private boolean isConversationMessage(Message message, User user1, User user2) {
        return (message.getTo().contains(user2) && message.getFrom().equals(user1)) ||
                (message.getTo().contains(user1) && message.getFrom().equals(user2));
    }
}