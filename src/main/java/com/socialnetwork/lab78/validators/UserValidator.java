package com.socialnetwork.lab78.validators;


import com.socialnetwork.lab78.domain.User;

/**
 * The UserValidator class implements the Validator interface for User entities.
 * It performs validation checks on user attributes such as first name, last name, and email.
 */
public class UserValidator implements Validator<User> {

    /**
     * Validates a User entity by checking its first name and last name attributes.
     *
     * @param entity The User entity to be validated.
     * @throws ValidationException If the validation checks fail for first name or last name.
     */
    @Override
    public void validate(User entity) throws ValidationException {
        validateFirstName(entity.getFirstName());
        validateLastName(entity.getLastName());
        validateEmail(entity.getEmail());
    }

    /**
     * Validates the first name of a user.
     *
     * @param firstName The first name to be validated.
     * @throws ValidationException If the first name is null, too long, empty, or doesn't start with a letter.
     */
    private void validateFirstName(String firstName) throws ValidationException {
        if (firstName == null)
            throw new ValidationException("First name must not be null!");
        else if (firstName.length() >= 100)
            throw new ValidationException("First name is too long!");
        else if (firstName.isEmpty())
            throw new ValidationException("First name must not be empty!");
        else if (!Character.isAlphabetic(firstName.charAt(0)))
            throw new ValidationException("First name must start with a letter!");
    }

    /**
     * Validates the last name of a user.
     *
     * @param lastName The last name to be validated.
     * @throws ValidationException If the last name is null, too long, empty, or doesn't start with a letter.
     */
    private void validateLastName(String lastName) throws ValidationException {
        if (lastName == null)
            throw new ValidationException("Last name must not be null!");
        else if (lastName.length() >= 100)
            throw new ValidationException("Last name is too long!");
        else if (lastName.isEmpty())
            throw new ValidationException("Last name must not be empty!");
        else if (!Character.isAlphabetic(lastName.charAt(0)))
            throw new ValidationException("Last name must start with a letter!");
    }

    /**
     * Validates the email of a user.
     *
     * @param email The email to be validated.
     * @throws ValidationException If the email is null, too long, empty, doesn't contain '@', or contains more than one '@'.
     */
    private void validateEmail(String email) throws ValidationException {
        if (email == null)
            throw new ValidationException("Email must not be null!");
        else if (email.length() >= 100)
            throw new ValidationException("Email is too long!");
        else if (email.isEmpty())
            throw new ValidationException("Email must not be empty!");
        else if (!email.contains("@"))
            throw new ValidationException("Email must contain @!");
        else if (email.indexOf('@') != email.lastIndexOf('@'))
            throw new ValidationException("Email must contain only one @!");
    }
}