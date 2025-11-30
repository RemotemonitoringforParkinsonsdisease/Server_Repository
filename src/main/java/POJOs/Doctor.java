package POJOs;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a doctor in the telemedicine system, storing personal information
 * such as full name and date of birth, the identifiers used in the database,
 * the password for authentication and the list of patients assigned to this doctor.
 */
public class Doctor{
    private String fullName;
    private Integer userId;
    private Integer doctorId;
    private String doctorPassword;
    private LocalDate dob;
    private List<Patient> patients;

    /**
     * Creates a new doctor using only the data provided at registration time
     * on the client side, including full name, password and date of birth.
     * The identifiers and patient list can be set later once the doctor is
     * stored in the database.
     *
     * @param fullname        the full name of the doctor
     * @param doctorPassword  the password used by the doctor to log in
     * @param dob             the date of birth of the doctor
     */
    public Doctor(String fullname, String doctorPassword, LocalDate dob) {
        this.fullName = fullname;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
    }

    /**
     * Creates a doctor with all fields typically loaded from the database,
     * including the user and doctor identifiers, password, date of birth,
     * list of assigned patients and full name.
     *
     * @param userId          the identifier of the associated user record
     * @param doctorId        the identifier of the doctor record
     * @param doctorPassword  the password used by the doctor to log in
     * @param dob             the date of birth of the doctor
     * @param patients        the list of patients assigned to this doctor
     * @param fullName        the full name of the doctor
     */
    public Doctor(Integer userId, Integer doctorId, String doctorPassword, LocalDate dob, List<Patient> patients, String fullName) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
        this.patients = patients;
        this.fullName = fullName;
    }

    /**
     * Returns the identifier of the user associated with this doctor.
     *
     * @return the user id linked to this doctor
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the identifier of the user associated with this doctor.
     *
     * @param userId the user id to be linked to this doctor
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the identifier of this doctor in the database.
     *
     * @return the doctor id
     */
    public Integer getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the identifier of this doctor in the database.
     *
     * @param doctorId the doctor id to assign
     */
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Returns the password used by this doctor to log in.
     *
     * @return the doctor password
     */
    public String getDoctorPassword() {
        return doctorPassword;
    }

    /**
     * Returns the date of birth of the doctor.
     *
     * @return the date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Returns the list of patients currently assigned to this doctor.
     *
     * @return the list of patients, or null if it has not been set
     */
    public List<Patient> getPatients() {
        return patients;
    }

    /**
     * Sets the list of patients assigned to this doctor.
     *
     * @param patients the list of patients to associate with this doctor
     */
    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    /**
     * Returns the full name of the doctor.
     *
     * @return the doctor's full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns a string representation of the doctor including full name,
     * identifiers, password and date of birth, mainly intended for logging
     * and debugging purposes.
     *
     * @return a string describing the main fields of this doctor
     */
    @Override
    public String toString() {
        return "Doctor{" +
                "fullName='" + fullName + '\'' +
                ", userId=" + userId +
                ", doctorId=" + doctorId +
                ", doctorPassword='" + doctorPassword + '\'' +
                ", dob=" + dob +
                '}';
    }
}
