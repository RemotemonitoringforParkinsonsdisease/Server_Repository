package jdbcs;

import POJOs.Doctor;
import managers.DoctorManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorJDBC implements DoctorManager {

    private ManagerJDBC manager;

    public DoctorJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    @Override
    public void addDoctor(String fullName, String email, int doctorId) {
        String sql = "INSERT INTO Doctor (full_name, email, doctor_id) VALUES (?,?,?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setInt(3, doctorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM Doctor WHERE doctor_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                return new Doctor(email, null, fullName, null, null); // Adaptar constructor según tus POJOs
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Doctor> readDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM Doctor";
        try (Statement stmt = manager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("doctor_id");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                doctors.add(new Doctor(email, null, fullName, null, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
}
