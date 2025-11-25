package jdbcs;

import POJOs.User;

import java.sql.*;

public class UserJDBC {
    private Connection c;
    private ManagerJDBC manager;
    public UserJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    public User getUserByEmail(String email){
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    public User addUser(String email) {
        String sql = "INSERT INTO User (email) VALUES (?)";

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

        return null; // error or duplicate email
    }
    public User getUserByEmail(String email) {

        String sql = "SELECT user_id, email FROM User WHERE email = ?";

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

        return null; // no user found
    }
}
