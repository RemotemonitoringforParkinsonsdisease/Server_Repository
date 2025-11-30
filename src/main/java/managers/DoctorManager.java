package managers;

import POJOs.Doctor;
import java.util.List;

/**
 * Contract for all doctor-related database operations.
 * Defines only the data-access methods required by the business layer.
 */
public interface DoctorManager {

    /**
     * Inserts a new doctor into the database.
     *
     * @param doctor the doctor to insert
     */
    void addDoctor(Doctor doctor);

    /**
     * Returns a random doctor ID from the database.
     *
     * @return the ID of a random doctor, or null if none exist
     */
    Integer getRandomDoctorId();

    /**
     * Retrieves a doctor by its database identifier.
     *
     * @param doctorId the doctor ID
     * @return the matching Doctor, or null if not found
     */
    Doctor getDoctorByDoctorId(Integer doctorId);

    /**
     * Retrieves the doctor ID associated with a given user ID.
     *
     * @param userId the user identifier
     * @return the doctor ID or null if not found
     */
    Integer getDoctorIdByUserId(Integer userId);

    /**
     * Retrieves all doctors stored in the database.
     *
     * @return a list of all doctors (possibly empty)
     */
    List<Doctor> readDoctors();

    /**
     * Retrieves the password for a given doctor ID.
     *
     * @param doctorId the doctor ID
     * @return the stored password or null if not found
     */
    String getPasswordByDoctorId(Integer doctorId);
}
