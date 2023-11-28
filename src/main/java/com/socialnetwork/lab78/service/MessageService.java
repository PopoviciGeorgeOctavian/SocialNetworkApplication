package com.socialnetwork.lab78.service;

import com.socialnetwork.lab78.domain.Message;
import com.socialnetwork.lab78.repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class MessageService {
    private final Repository<UUID, Message> messageRepo;

    public MessageService(Repository<UUID, Message> messageRepo) {
        this.messageRepo = messageRepo;
    }

    public boolean addMessage(Message message) {
        Optional<Message> existingMessage = messageRepo.save(message);

        if (existingMessage.isPresent()) {
            System.err.println("A message with this ID already exists!");
            return false;
        }

        return true;
    }

    public Message deleteMessage(UUID id) {
        Optional<Message> deletedMessage = messageRepo.delete(id);

        if (deletedMessage.isPresent()) {
            return deletedMessage.get();
        } else {
            // Handle the case where the message with the given ID doesn't exist.
            return null;
        }
    }

    public Iterable<Message> getAllMessages() {
        ArrayList<Message> messageList = new ArrayList<>();
        messageRepo.findAll().forEach(messageList::add);

        messageList.sort(Comparator.comparing(Message::getDate));

        return messageList;
    }
}
