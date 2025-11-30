package POJOs;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a clinical report in the telemedicine system, storing the
 * identifier of the report and patient, the date of the report, the path
 * to the associated signals file, the list of reported symptoms and the
 * observations written by both the patient and the doctor.
 */
public class Report {

    private Integer reportId;
    private Integer patientId;
    private LocalDate reportDate;
    private String signalsFilePath;
    private List<Symptoms> symptoms;
    private String patientObservation;
    private String doctorObservation;

    /**
     * Creates a new report with the main information provided by the patient
     * and the system, including the patient identifier, report date, patient
     * and doctor observations, the list of symptoms and the path to the file
     * where the recorded signals are stored.
     *
     * @param patientId          the identifier of the patient to whom this report belongs
     * @param reportDate         the date on which the report was created
     * @param patientObservation the observation text written by the patient
     * @param doctorObservation  the observation text written by the doctor, which may initially be empty
     * @param symptoms           the list of symptoms associated with this report
     * @param signalsFilePath    the path to the signals file linked to this report
     */
    public Report(Integer patientId, LocalDate reportDate, String patientObservation, String doctorObservation, List<Symptoms> symptoms, String signalsFilePath) {
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
        this.symptoms = symptoms;
        this.signalsFilePath = signalsFilePath;
    }

    /**
     * Returns the identifier of this report in the database.
     *
     * @return the report id
     */
    public Integer getReportId() {
        return reportId;
    }

    /**
     * Sets the identifier of this report in the database.
     *
     * @param reportId the report id to assign
     */
    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    /**
     * Returns the identifier of the patient to whom this report belongs.
     *
     * @return the patient id linked to this report
     */
    public Integer getPatientId() {
        return patientId;
    }

    /**
     * Returns the date on which this report was created.
     *
     * @return the report date
     */
    public LocalDate getReportDate() {
        return reportDate;
    }

    /**
     * Returns the list of symptoms associated with this report.
     *
     * @return the list of symptoms
     */
    public List<Symptoms> getSymptoms() {
        return symptoms;
    }

    /**
     * Returns the observation text written by the patient.
     *
     * @return the patient observation
     */
    public String getPatientObservation() {
        return patientObservation;
    }

    /**
     * Returns the observation text written by the doctor.
     *
     * @return the doctor observation
     */
    public String getDoctorObservation() {
        return doctorObservation;
    }

    /**
     * Returns the path to the file that stores the signals associated with
     * this report.
     *
     * @return the signals file path
     */
    public String getSignalsFilePath() {
        return signalsFilePath;
    }

    /**
     * Returns a string representation of the report including its identifiers,
     * date, signals file path, symptoms and both patient and doctor observations,
     * mainly intended for logging and debugging purposes.
     *
     * @return a string describing the main fields of this report
     */
    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", patientId=" + patientId +
                ", reportDate=" + reportDate +
                ", signalsFilePath=" + signalsFilePath +
                ", symptoms=" + symptoms +
                ", patientObservation='" + patientObservation + '\'' +
                ", doctorObservation='" + doctorObservation + '\'' +
                '}';
    }
}
