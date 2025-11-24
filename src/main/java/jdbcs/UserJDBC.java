package jdbcs;

import POJOs.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserJDBC {
    private ManagerJDBC manager;

    public UserJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }
    public User getUserByEmail(String email){
        String query = "SELECT * FROM users WHERE email = ?";
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
    public void addUser(){
        String query = "INSERT INTO users (email) VALUES (?)";
    }
}
