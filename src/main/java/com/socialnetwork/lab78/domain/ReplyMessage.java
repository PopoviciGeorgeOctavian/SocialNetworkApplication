package com.socialnetwork.lab78.domain;

import java.util.List;

class ReplyMessage extends Message {
    private Message repliedMessage;

    public ReplyMessage(User sender, List<User> recipients, String text, Message repliedMessage) {
        super(sender, recipients, text);
        this.repliedMessage = repliedMessage;
    }

    public Message getRepliedMessage() {
        return repliedMessage;
    }

    public void setRepliedMessage(Message repliedMessage) {
        this.repliedMessage = repliedMessage;
    }
}
