package managers;

import POJOs.Report;
import java.util.List;

/**
 * Defines the operations related to report management at a higher level,
 * including adding new reports, retrieving all reports for a given patient
 * and updating the doctor's observation of an existing report.
 */
public interface ReportManager {

    /**
     * Adds a new report to the system using the data contained in the given
     * Report object. The specific persistence mechanism is defined by the
     * implementing class.
     *
     * @param report the report entity that will be stored in the system
     */
    void addReport(Report report);

    /**
     * Retrieves all reports associated with the given patient identifier.
     * The implementing class should return a list of Report objects, or an
     * empty list if the patient has no reports.
     *
     * @param patientId the identifier of the patient whose reports are requested
     * @return a list containing all reports for the given patient, possibly empty
     */
    List<Report> getReportsByPatientId(Integer patientId);

    /**
     * Updates the doctor observation field of the report identified by the
     * given report identifier. If no report exists with that id, the
     * implementing class may choose to do nothing or to signal the situation
     * in an appropriate way.
     *
     * @param reportId          the identifier of the report to be updated
     * @param doctorObservation the new observation text written by the doctor
     */
    void updateDoctorObservation(Integer reportId, String doctorObservation);
}
