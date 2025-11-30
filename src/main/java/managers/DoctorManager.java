package managers;

import POJOs.Doctor;
import java.util.List;

/**
 * Defines the operations related to doctor management at a higher level,
 * including adding new doctors, obtaining doctor identifiers, retrieving
 * doctor records and listing all doctors stored in the system.
 */
public interface DoctorManager {

    /**
     * Adds a new doctor to the system using the data contained in the given
     * Doctor object. The specific persistence mechanism is defined by the
     * implementing class.
     *
     * @param doctor the doctor entity that will be stored in the system
     */
    void addDoctor(Doctor doctor);

    /**
     * Returns the identifier of a randomly selected doctor from all doctors
     * available in the system. If there are no doctors, the implementing
     * class may return null or handle the situation in another way.
     *
     * @return the identifier of a random doctor, or null if no doctor is available
     */
    Integer getRandomDoctorId();

    /**
     * Retrieves the doctor associated with the given doctor identifier and
     * returns it as a Doctor object. If no doctor exists with that id, the
     * implementing class should return null.
     *
     * @param doctorId the identifier of the doctor to be retrieved
     * @return the Doctor object matching the id, or null if it cannot be found
     */
    Doctor getDoctorByDoctorId(Integer doctorId);

    /**
     * Retrieves the doctor identifier associated with a given user identifier.
     * If there is no doctor linked to that user, the implementing class
     * should return null.
     *
     * @param userId the identifier of the user whose doctor id is requested
     * @return the doctor id for the given user, or null if no doctor is linked
     */
    Integer getDoctorIdByUserId(Integer userId);

    /**
     * Returns a list with all doctors stored in the system. If there are no
     * doctors, the implementing class should return an empty list rather than null.
     *
     * @return a list containing all doctors in the system, possibly empty
     */
    List<Doctor> readDoctors();

    /**
     * Retrieves the password associated with the given doctor identifier.
     * If no doctor exists with that id, the implementing class should
     * return null.
     *
     * @param doctorId the identifier of the doctor whose password is requested
     * @return the password of the doctor, or null if it cannot be found
     */
    String getPasswordByDoctorId(Integer doctorId);
}
