package com.socialnetwork.lab78.validators;


import com.socialnetwork.lab78.domain.FriendShip;

/**
 * The FriendshipValidator class implements the Validator interface for FriendShip entities.
 * It ensures that FriendShip objects have users with different IDs.
 */
public class FriendshipValidator implements Validator<FriendShip> {

    /**
     * Validates a FriendShip entity to ensure that its user1 and user2 have different IDs.
     *
     * @param entity The FriendShip entity to be validated.
     * @throws ValidationException If the user IDs are the same, indicating an invalid friendship.
     */
    @Override
    public void validate(FriendShip entity) throws ValidationException {
        // Check if user1 and user2 have different IDs
        if (entity.getUser1().getId().equals(entity.getUser2().getId())) {
            throw new ValidationException("FriendShip's user must have different ids");
        }
    }
}