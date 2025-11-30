package POJOs;

/**
 * Represents a generic user in the system, storing the identifier used in the
 * database and the email address associated with that account.
 */
public class User {
    private Integer id;   // protegido para que subclases puedan acceder
    private String email;

    /**
     * Creates an empty user instance whose fields can be set later using the
     * corresponding setter methods.
     */
    public User() {
    }

    /**
     * Returns the identifier of this user in the database.
     *
     * @return the user id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the email address associated with this user.
     *
     * @return the user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the identifier of this user in the database.
     *
     * @param id the user id to assign
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Sets the email address associated with this user.
     *
     * @param email the email address to assign to this user
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
