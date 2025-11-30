package POJOs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in the telemedicine system, storing personal information
 * such as full name and date of birth, the identifiers used in the database,
 * the password for authentication, the identifier of the assigned doctor and
 * the list of clinical reports associated with this patient.
 */
public class Patient{
    private String fullName;
    private Integer userId;
    private Integer patientId;
    private Integer doctorId;
    private String patientPassword;
    private LocalDate dob;
    private List<Report> reports;

    /**
     * Creates a patient with the main fields typically loaded from the database,
     * including full name, user and patient identifiers, doctor identifier,
     * password and date of birth. The list of reports can be set later.
     *
     * @param fullName        the full name of the patient
     * @param userId          the identifier of the associated user record
     * @param patientId       the identifier of the patient record
     * @param doctorId        the identifier of the doctor assigned to this patient
     * @param patientPassword the password used by the patient to log in
     * @param dob             the date of birth of the patient
     */
    public Patient(String fullName, Integer userId, Integer patientId, Integer doctorId, String patientPassword, LocalDate dob) {
        this.fullName = fullName;
        this.userId = userId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientPassword = patientPassword;
        this.dob = dob;
    }

    /**
     * Creates a patient with all fields typically loaded from the database,
     * including full name, identifiers, password, date of birth and the list
     * of clinical reports associated with this patient.
     *
     * @param fullName        the full name of the patient
     * @param userId          the identifier of the associated user record
     * @param patientId       the identifier of the patient record
     * @param doctorId        the identifier of the doctor assigned to this patient
     * @param patientPassword the password used by the patient to log in
     * @param dob             the date of birth of the patient
     * @param reports         the list of clinical reports linked to this patient
     */
    public Patient(String fullName,Integer userId, Integer patientId, Integer doctorId, String patientPassword, LocalDate dob, List<Report> reports) {
        this.fullName = fullName;
        this.userId = userId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientPassword = patientPassword;
        this.dob = dob;
        this.reports = reports;
    }

    /**
     * Creates a new patient using only the data provided at registration time
     * on the client side, including password, full name and date of birth.
     * The identifiers and doctor assignment can be set later once the patient
     * is stored in the database.
     *
     * @param password the password used by the patient to log in
     * @param fullName the full name of the patient
     * @param dob      the date of birth of the patient
     */
    public Patient(String password, String fullName, LocalDate dob) {
        this.fullName = fullName;
        this.dob = dob;
        this.patientPassword = password;
    }

    /**
     * Returns the identifier of the user associated with this patient.
     *
     * @return the user id linked to this patient
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the identifier of the user associated with this patient.
     *
     * @param userId the user id to be linked to this patient
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the identifier of this patient in the database.
     *
     * @return the patient id
     */
    public Integer getPatientId() {
        return patientId;
    }

    /**
     * Sets the identifier of this patient in the database.
     *
     * @param patientId the patient id to assign
     */
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    /**
     * Returns the identifier of the doctor assigned to this patient.
     *
     * @return the doctor id linked to this patient
     */
    public Integer getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the identifier of the doctor assigned to this patient.
     *
     * @param doctorId the doctor id to link to this patient
     */
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Returns the password used by this patient to log in.
     *
     * @return the patient password
     */
    public String getPatientPassword() {
        return patientPassword;
    }

    /**
     * Returns the date of birth of the patient.
     *
     * @return the date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Returns the list of clinical reports associated with this patient.
     *
     * @return the list of reports, or null if it has not been set
     */
    public List<Report> getReports() {
        return reports;
    }

    /**
     * Sets the list of clinical reports associated with this patient.
     *
     * @param reports the list of reports to link to this patient
     */
    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    /**
     * Adds a new clinical report to the list of reports associated with this
     * patient. If the list is null, it is created before adding the report.
     *
     * @param report the report to add to this patient
     */
    public void addReport(Report report) {
        if (this.reports == null) {
            this.reports = new ArrayList<>();
        }
        this.reports.add(report);
    }

    /**
     * Returns the full name of the patient.
     *
     * @return the patient's full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns a string representation of the patient including full name,
     * identifiers, password and date of birth, mainly intended for logging
     * and debugging purposes.
     *
     * @return a string describing the main fields of this patient
     */
    @Override
    public String toString() {
        return "Patient{" +
                "fullName='" + fullName + '\'' +
                ", userId=" + userId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", patientPassword='" + patientPassword + '\'' +
                ", dob=" + dob +
                '}';
    }
}
