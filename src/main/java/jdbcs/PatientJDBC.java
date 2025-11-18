package jdbcs;

import POJOs.Patient;
import POJOs.Doctor;
import managers.PatientManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientJDBC implements PatientManager {

    private ManagerJDBC manager;

    public PatientJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    @Override
    public void addPatient(String fullName, String email, int doctorId, int patientId) {
        String sql = "INSERT INTO Patient (full_name, email, doctor_id, patient_id) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setInt(3, doctorId);
            stmt.setInt(4, patientId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM Patient WHERE patient_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String dobString = rs.getString("dob");
                LocalDate dob = (dobString != null) ? LocalDate.parse(dobString) : null;

                // Por ahora el doctor lo ponemos a null, luego puedes implementarlo con DoctorJDBC
                Doctor doctor = null;

                return new Patient(id, fullName, dob, email, doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Patient> readPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient";
        try (Statement stmt = manager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String fullName = rs.getString("full_name");
                String dobString = rs.getString("dob");
                LocalDate dob = (dobString != null) ? LocalDate.parse(dobString) : null;
                String email = rs.getString("email");

                Doctor doctor = null; // Se puede implementar luego

                patients.add(new Patient(id, fullName, dob, email, doctor));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
}
