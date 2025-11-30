package managers;

import POJOs.Patient;
import java.util.List;

/**
 * Contract for all patient-related database operations.
 * Defines only the data-access operations required by the business layer.
 */
public interface PatientManager {

    /**
     * Inserts a new patient into the database.
     *
     * @param patient the patient to insert
     */
    void addPatient(Patient patient);

    /**
     * Retrieves a patient by its patient identifier.
     *
     * @param patientId the patient ID
     * @return the matching Patient or null if not found
     */
    Patient getPatientByPatientId(Integer patientId);

    /**
     * Retrieves the patient password for a given patient ID.
     *
     * @param patientId the patient ID
     * @return the password or null if not found
     */
    String getPasswordByPatientId(Integer patientId);

    /**
     * Retrieves the patient ID associated with the given user ID.
     *
     * @param userId the user identifier
     * @return the matching patient ID or null
     */
    Integer getPatientIdByUserId(Integer userId);

    /**
     * Retrieves a patient by its email address.
     *
     * @param email the email used to search
     * @return the matching Patient or null
     */
    Patient getPatientByEmail(String email);

    /**
     * Retrieves all patients assigned to a specific doctor.
     *
     * @param doctorId the doctor's ID
     * @return a list of patients (possibly empty)
     */
    List<Patient> getPatientsByDoctorId(Integer doctorId);
}
