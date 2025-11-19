package jdbcs;

import POJOs.Doctor;
import managers.DoctorManager;
import java.sql.*;
import java.time.LocalDate;
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
    public void addDoctor(Doctor doctor) {
        String sql = "INSERT INTO Doctor (doctor_id, full_name, dob, email, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, doctor.getId());  // tu id aleatorio con "d" al inicio
            stmt.setString(2, doctor.getFullName());
            stmt.setString(3, doctor.getDob().toString());
            stmt.setString(4, doctor.getEmail());
            stmt.setString(5, doctor.getPassword());
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

    @Override
    public Doctor getDoctorByEmail(String email) {
        String sql = "SELECT * FROM Doctor WHERE email = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String id = rs.getString("doctor_id");
                String fullName = rs.getString("full_name");
                String dobStr = rs.getString("dob");
                String password = rs.getString("password");

                LocalDate dob = LocalDate.parse(dobStr);

                return new Doctor(id, fullName, dob, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
