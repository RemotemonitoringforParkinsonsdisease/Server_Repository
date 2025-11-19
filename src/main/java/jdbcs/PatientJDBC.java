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
    public void addPatient(Patient patient) {
        String sql = "INSERT INTO Patient (patient_id, full_name, dob, email, password, doctor_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, patient.getId());
            stmt.setString(2, patient.getFullName());
            stmt.setString(3, patient.getDob().toString());
            stmt.setString(4, patient.getEmail());
            stmt.setString(5, patient.getPassword());
            stmt.setString(6, patient.getDoctorId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Patient getPatientById(String id) {
        String sql = "SELECT * FROM Patient WHERE patient_id = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String email = rs.getString("email");
                String password = rs.getString("password");
                String doctorId = rs.getString("doctor_id");

                Patient p = new Patient(id, fullName, dob, email, password);
                p.setDoctorId(doctorId);
                return p;
            }
        } catch (Exception e) {
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

    @Override
    public Patient getPatientByEmail(String email) {
        String sql = "SELECT * FROM Patient WHERE email = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String id = rs.getString("patient_id");
                String fullName = rs.getString("full_name");
                String dobStr = rs.getString("dob");
                String password = rs.getString("password");
                String doctorId = rs.getString("doctor_id");

                LocalDate dob = LocalDate.parse(dobStr);

                Patient p = new Patient(id, fullName, dob, email, password);
                p.setDoctorId(doctorId);
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Patient> getPatientsByDoctor(String doctorId) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient WHERE doctor_id = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("patient_id");
                String fullName = rs.getString("full_name");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String email = rs.getString("email");
                String password = rs.getString("password");

                Patient p = new Patient(id, fullName, dob, email, password);
                p.setDoctorId(doctorId);
                patients.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patients;
    }

}
