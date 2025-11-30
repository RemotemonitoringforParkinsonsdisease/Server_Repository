package managers;

import POJOs.Report;
import java.util.List;

/**
 * Contract for all report-related database operations.
 * Defines only the data-access operations required by the business layer.
 */
public interface ReportManager {

    /**
     * Inserts a new report into the database.
     *
     * @param report the report to insert
     */
    void addReport(Report report);

    /**
     * Retrieves all reports belonging to a specific patient.
     *
     * @param patientId the patient identifier
     * @return list of reports associated with the patient
     */
    List<Report> getReportsByPatientId(Integer patientId);

    /**
     * Updates the doctor's observation in a specific report.
     *
     * @param reportId          the report identifier
     * @param doctorObservation the new observation
     */
    void updateDoctorObservation(Integer reportId, String doctorObservation);

    /**
     * Retrieves a report ID based on its signals file path.
     *
     * @param signalsFilePath the file path stored in the report
     * @return the matching report ID, or null if not found
     */
    Integer getReportIdBySignalFilePath(String signalsFilePath);
}
