package jdbcs;

import POJOs.Patient;
import POJOs.Report;
import managers.PatientManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientJDBC implements PatientManager {

    private ManagerJDBC manager;

    // Constructor
    public PatientJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    // Método para agregar un paciente
    public void addPatient(Patient patient) {
        String sql = "INSERT INTO patient (user_id, patient_id, doctor_id, full_name, dob, patient_password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, patient.getUserId());
            stmt.setInt(2, patient.getPatientId());
            stmt.setInt(3, patient.getDoctorId());
            stmt.setString(4, patient.getFullName());
            stmt.setString(5, patient.getDob().toString());
            stmt.setString(6, patient.getPatientPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener un paciente por su patient_id
    public Patient getPatientByPatientId(Integer patientId) {
        String sql = "SELECT * FROM patient WHERE patient_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, patientId); // Establecer el patient_id en la consulta SQL
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Integer doctorId = rs.getInt("doctor_id");
                String fullName = rs.getString("full_name");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String patientPassword = rs.getString("patient_password");

                // Crear el objeto Patient con los datos obtenidos de la base de datos
                Patient patient = new Patient(fullName, userId, patientId, doctorId, patientPassword, dob);

                // Aquí puedes agregar más lógica si deseas incluir la lista de reportes
                // Si tienes un método como getReportsByPatientId, puedes llamarlo aquí y agregar los reportes al paciente:
                List<Report> reports = manager.getReportJDBC().getReportsByPatientId(patientId);
                patient.setReports(reports);

                return patient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el paciente, se devuelve null
    }

    // Método para obtener todos los pacientes
    public List<Patient> readPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient";
        try (Statement stmt = manager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Integer patientId = rs.getInt("patient_id");
                Integer doctorId = rs.getInt("doctor_id");
                String fullName = rs.getString("full_name");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String patientPassword = rs.getString("patient_password");

                patients.add(new Patient(fullName, userId, patientId, doctorId, patientPassword, dob));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public String getPasswordByPatientId(Integer patientId) {
        String sql = "SELECT patient_password FROM patient WHERE patient_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, patientId); // Establecer el patient_id en la consulta SQL
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("patient_password");  // Devolver la contraseña del paciente
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra la contraseña, se devuelve null
    }


    public Integer getPatientIdByUserId(Integer userId) {
        String sql = "SELECT patient_id FROM patient WHERE user_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId); // Establecer el user_id en la consulta SQL
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("patient_id"); // Retorna el patient_id encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el paciente, se devuelve null
    }

    // Método para obtener un paciente por su email (suponiendo que hay un campo email en la base de datos)
    public Patient getPatientByEmail(String email) {
        String sql = "SELECT * FROM patient WHERE email=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Integer patientId = rs.getInt("patient_id");
                Integer doctorId = rs.getInt("doctor_id");
                String fullName = rs.getString("full_name");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String patientPassword = rs.getString("patient_password");

                return new Patient(fullName, userId, patientId, doctorId, patientPassword, dob);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obtener todos los pacientes asignados a un doctor
    public List<Patient> getPatientsByDoctor(Integer doctorId) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient WHERE doctor_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Integer patientId = rs.getInt("patient_id");
                Integer doctorIdDb = rs.getInt("doctor_id");
                String fullName = rs.getString("full_name");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String patientPassword = rs.getString("patient_password");

                patients.add(new Patient(fullName, userId, patientId, doctorIdDb, patientPassword, dob));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
    public Patient getPatientByUserId(Integer userId) {
        String sql = "SELECT * FROM patient WHERE user_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId); // Establecer el user_id en la consulta SQL
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Obtener los datos del paciente
                Integer patientId = rs.getInt("patient_id");
                Integer doctorId = rs.getInt("doctor_id");
                String fullName = rs.getString("full_name");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String patientPassword = rs.getString("patient_password");

                // Obtener los reportes del paciente
                List<Report> reports = manager.getReportJDBC().getReportsByPatientId(patientId);  // Llamamos al ReportJDBC para obtener los reportes

                // Crear el objeto Patient con los datos obtenidos de la base de datos
                return new Patient(fullName, userId, patientId, doctorId, patientPassword, dob, reports);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el paciente, se devuelve null
    }



}
