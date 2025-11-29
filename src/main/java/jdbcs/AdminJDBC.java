package jdbcs;

import POJOs.Admin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminJDBC {
    private ManagerJDBC manager;

    public AdminJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    public ManagerJDBC getManagerJDBC() {
        return manager;
    }

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
    public String getPasswordByAdminId(Integer adminId) {
        String sql = "SELECT admin_password FROM admin WHERE admin_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, adminId); //
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("admin_password");  // Devolver la contraseña del doctor
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra la contraseña, se devuelve null
    }
    public Integer getAdminIdByUserId(Integer userId) {
        String sql = "SELECT admin_id FROM admin WHERE user_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("admin_id");
            }// ✔
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}