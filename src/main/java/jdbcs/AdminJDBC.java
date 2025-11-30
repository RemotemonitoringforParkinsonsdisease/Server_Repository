package jdbcs;

import POJOs.Admin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides database access methods related to administrators, including
 * inserting new admin records and retrieving admin identifiers and passwords.
 * It uses the shared ManagerJDBC instance to obtain the database connection.
 */
public class AdminJDBC {
    private ManagerJDBC manager;

    /**
     * Creates a new AdminJDBC helper bound to the given JDBC manager, which
     * will be used to access the underlying database connection.
     *
     * @param manager the JDBC manager that provides the database connection
     */
    public AdminJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    /**
     * Returns the JDBC manager associated with this helper, which is used
     * to obtain the database connection and other JDBC-related resources.
     *
     * @return the ManagerJDBC instance used by this class
     */
    public ManagerJDBC getManagerJDBC() {
        return manager;
    }

    /**
     * Inserts a new admin record into the database using the user identifier
     * and password stored in the given Admin object. If an SQL error occurs,
     * the exception is printed and the method finishes without throwing it.
     *
     * @param admin the admin entity containing the user id and password to store
     */
    public void addAdmin(Admin admin) {
        String sql = "INSERT INTO admin (user_id, admin_password) VALUES (?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, admin.getUserId());
            stmt.setString(2, admin.getAdminPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the password associated with the given admin identifier from
     * the database. If there is no admin with that id or an error occurs, the
     * method returns null.
     *
     * @param adminId the identifier of the admin whose password is requested
     * @return the admin password, or null if it cannot be found or an error occurs
     */
    public String getPasswordByAdminId(Integer adminId) {
        String sql = "SELECT admin_password FROM admin WHERE admin_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("admin_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the admin identifier associated with the given user identifier
     * from the database. If there is no admin linked to that user or an error
     * occurs, the method returns null.
     *
     * @param userId the identifier of the user whose admin id is requested
     * @return the admin id for the given user, or null if it cannot be found
     */
    public Integer getAdminIdByUserId(Integer userId) {
        String sql = "SELECT admin_id FROM admin WHERE user_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("admin_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
