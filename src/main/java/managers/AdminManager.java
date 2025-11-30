package managers;
import POJOs.Admin;

/**
 * Contract for all admin-related database operations.
 * Defines only the public data-access operations needed by the business layer.
 */
public interface AdminManager {

    /**
     * Inserts a new admin into the database.
     *
     * @param admin the admin to insert
     */
    void addAdmin(Admin admin);

    /**
     * Retrieves the password associated with the given admin ID.
     *
     * @param adminId the admin identifier
     * @return the stored admin password, or null if not found
     */
    String getPasswordByAdminId(Integer adminId);

    /**
     * Retrieves the admin ID associated with the given user ID.
     *
     * @param userId the user identifier
     * @return the admin identifier, or null if not found
     */
    Integer getAdminIdByUserId(Integer userId);
}
