package managers;

import POJOs.Patient;

/**
 * Defines the operations related to patient management at a higher level,
 * including adding new patients, retrieving patient records and obtaining
 * identifiers and passwords based on different keys.
 */
public interface PatientManager {

    /**
     * Adds a new patient to the system using the data contained in the given
     * Patient object. The specific persistence mechanism is defined by the
     * implementing class.
     *
     * @param patient the patient entity that will be stored in the system
     */
    void addPatient(Patient patient);

    /**
     * Retrieves the patient associated with the given patient identifier and
     * returns it as a Patient object. If no patient exists with that id, the
     * implementing class should return null.
     *
     * @param patientId the identifier of the patient to be retrieved
     * @return the Patient object matching the id, or null if it cannot be found
     */
    Patient getPatientByPatientId(Integer patientId);

    /**
     * Retrieves the password associated with the given patient identifier.
     * If no patient exists with that id, the implementing class should
     * return null.
     *
     * @param patientId the identifier of the patient whose password is requested
     * @return the password of the patient, or null if it cannot be found
     */
    String getPasswordByPatientId(Integer patientId);

    /**
     * Retrieves the patient identifier associated with a given user identifier.
     * If there is no patient linked to that user, the implementing class
     * should return null.
     *
     * @param userId the identifier of the user whose patient id is requested
     * @return the patient id for the given user, or null if no patient is linked
     */
    Integer getPatientIdByUserId(Integer userId);

}
