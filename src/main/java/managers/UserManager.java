package managers;

import POJOs.User;

/**
 * Contract for all user-related database operations.
 * Defines only the public data-access operations required by the business layer.
 */
public interface UserManager {

    /**
     * Inserts a new user using the given email.
     *
     * @param email the email for the new user
     * @return the created User with its generated ID, or null if insertion fails
     */
    User addUser(String email);

    /**
     * Retrieves a user by its email.
     *
     * @param email the email to search
     * @return the matching User, or null if not found
     */
    User getUserByEmail(String email);

    /**
     * Retrieves the user ID associated with the given email.
     *
     * @param email the email to search
     * @return the user ID, or null if not found
     */
    Integer getUserIdByEmail(String email);

    /**
     * Retrieves a user by its database identifier.
     *
     * @param id the user ID
     * @return the matching User, or null if not found
     */
    User getUserById(int id);
}
