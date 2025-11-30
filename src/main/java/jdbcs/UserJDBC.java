package jdbcs;

import POJOs.User;

import java.sql.*;

/**
 * Provides JDBC-based access to user data, including inserting new users
 * and retrieving user records or identifiers by email or id. It uses the
 * shared ManagerJDBC instance to obtain the database connection.
 */
public class UserJDBC {
    private Connection c;
    private ManagerJDBC manager;

    /**
     * Creates a new UserJDBC helper bound to the given JDBC manager. The
     * constructor obtains an initial database connection that is reused for
     * some operations, while others create a new connection on demand.
     *
     * @param manager the JDBC manager that provides the database connection
     */
    public UserJDBC(ManagerJDBC manager) {
        this.manager = manager;
        this.c = manager.getConnection();
    }

    /**
     * Inserts a new user record into the database using the given email
     * address. If the insertion succeeds, the method returns a User object
     * with the generated identifier and the stored email. If an error occurs
     * or the email is duplicated and cannot be inserted, it returns null.
     *
     * @param email the email address to be stored for the new user
     * @return the created User with its generated id, or null if it cannot be inserted
     */
    public User addUser(String email) {
        String sql = "INSERT INTO user (email) VALUES (?)";

        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt(1));
                    user.setEmail(email);
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves a user record from the database using its email address.
     * If a user with that email exists, the method returns a User object
     * with its identifier and email; otherwise it returns null.
     *
     * @param email the email address used to search for the user
     * @return the User matching the email, or null if no user is found
     */
    public User getUserByEmail(String email) {

        String sql = "SELECT user_id, email FROM user WHERE email = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves the identifier of a user given its email address. The method
     * searches the user table and, if a match is found, returns the user_id
     * column for that row. If no user is found or an error occurs, it returns null.
     *
     * @param email the email address used to search for the user id
     * @return the user id associated with the email, or null if not found
     */
    public Integer getUserIdByEmail(String email) {
        String sql = "SELECT user_id FROM user WHERE email = ?";

        try (Connection conn = manager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves a user record from the database using its identifier. If a
     * user with that id exists, the method returns a User object with its
     * identifier and email; otherwise it returns null.
     *
     * @param id the identifier of the user to look up
     * @return the User matching the id, or null if no user is found
     */
    public User getUserById(int id) {

        String sql = "SELECT user_id, email FROM user WHERE user_id = ?";

        try (Connection conn = manager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
