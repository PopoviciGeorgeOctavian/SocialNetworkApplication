package com.socialnetwork.lab78.validators;

import com.socialnetwork.lab78.domain.Message;

public class MessageValidator implements Validator<Message>
{/**
 * FriendShip's user must have different ids
 */
@Override
public void validate(Message entity) throws ValidationException {
    if(entity.getMessageText().contains(";"))
        throw new ValidationException("Textul nu poate contine cacarterul ';'");
}
}

