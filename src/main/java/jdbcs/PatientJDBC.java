package jdbcs;

import POJOs.Patient;
import POJOs.Report;
import managers.PatientManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides JDBC-based access to patient data, including inserting new patients,
 * retrieving patient records by different keys and listing patients assigned
 * to a specific doctor. It uses the shared ManagerJDBC instance to obtain the
 * database connection and execute SQL queries.
 */
public class PatientJDBC implements PatientManager {

    private ManagerJDBC manager;

    /**
     * Creates a new PatientJDBC helper bound to the given JDBC manager, which
     * will be used to obtain the database connection and execute all patient
     * related queries.
     *
     * @param manager the JDBC manager that provides the database connection
     */
    public PatientJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    /**
     * Inserts a new patient record into the database using the information
     * stored in the given Patient object. The method stores the user identifier,
     * doctor identifier, full name, date of birth and patient password. If an
     * SQL error occurs, the exception is printed and the method finishes without
     * throwing it.
     *
     * @param patient the patient entity containing the data to be stored
     */
    public void addPatient(Patient patient) {
        String sql = "INSERT INTO patient (user_id, doctor_id, full_name, dob, patient_password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, patient.getUserId());
            stmt.setInt(2, patient.getDoctorId());
            stmt.setString(3, patient.getFullName());
            stmt.setString(4, patient.getDob().toString());
            stmt.setString(5, patient.getPatientPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a patient record from the database using its patient identifier.
     * It builds a Patient object with the stored values for that id, loads the
     * list of reports associated with the patient and returns the complete
     * Patient instance. If no patient is found with the given identifier or an
     * error occurs, it returns null.
     *
     * @param patientId the identifier of the patient to look up
     * @return the Patient object for the given id, or null if it cannot be found
     */
    public Patient getPatientByPatientId(Integer patientId) {
        String sql = "SELECT * FROM patient WHERE patient_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Integer doctorId = rs.getInt("doctor_id");
                String fullName = rs.getString("full_name");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String patientPassword = rs.getString("patient_password");

                Patient patient = new Patient(fullName, userId, patientId, doctorId, patientPassword, dob);

                List<Report> reports = manager.getReportJDBC().getReportsByPatientId(patientId);
                patient.setReports(reports);

                return patient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the password of a patient given its patient identifier. The
     * method queries the patient table and returns the stored password for that
     * id. If no patient is found or an error occurs, it returns null.
     *
     * @param patientId the identifier of the patient whose password is requested
     * @return the password of the patient, or null if it cannot be found
     */
    public String getPasswordByPatientId(Integer patientId) {
        String sql = "SELECT patient_password FROM patient WHERE patient_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("patient_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the patient identifier associated with the given user identifier.
     * It searches the patient table for a row whose user_id matches the provided
     * value and, if found, returns the corresponding patient_id. If no patient is
     * linked to that user or an error occurs, the method returns null.
     *
     * @param userId the user identifier linked to the patient
     * @return the patient id for the given user, or null if it cannot be found
     */
    public Integer getPatientIdByUserId(Integer userId) {
        String sql = "SELECT patient_id FROM patient WHERE user_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("patient_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a patient record from the database using an email value,
     * assuming that the patient table stores an email column. It builds and
     * returns a Patient object with the stored values. If no patient is found
     * with the given email or an error occurs, it returns null.
     *
     * @param email the email address used to search for the patient
     * @return the Patient object matching the email, or null if it cannot be found
     */
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

    /**
     * Retrieves all patients assigned to a given doctor identifier and returns
     * them as a list of Patient objects. If no patients are found, the method
     * returns an empty list. If an SQL error occurs, the exception is printed
     * and the list accumulated up to that point is returned.
     *
     * @param doctorId the identifier of the doctor whose patients are requested
     * @return a list with all patients linked to the given doctor, possibly empty
     */
    public List<Patient> getPatientsByDoctorId(Integer doctorId) {
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

}
