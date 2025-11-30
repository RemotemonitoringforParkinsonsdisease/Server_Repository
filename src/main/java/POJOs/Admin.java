package POJOs;

/**
 * Represents an administrator user of the system, storing the identifier of the
 * associated user record and the password used to authenticate as an administrator.
 */
public class Admin {
    private Integer userId;
    private String adminPassword;

    /**
     * Creates a new admin with the given user identifier and administrator password.
     *
     * @param userId        the identifier of the user associated with this admin
     * @param adminPassword the password used to authenticate the administrator
     */
    public Admin(Integer userId, String adminPassword) {
        this.userId = userId;
        this.adminPassword = adminPassword;
    }

    /**
     * Returns the password used by this administrator to log in.
     *
     * @return the administrator password
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Returns the identifier of the user associated with this administrator.
     *
     * @return the user id linked to this admin
     */
    public int getUserId() {
        return userId;
    }
}
