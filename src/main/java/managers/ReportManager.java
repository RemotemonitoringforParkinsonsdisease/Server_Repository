package managers;

import POJOs.Report;
import java.util.List;

public interface ReportManager {
    void addReport(Report report);
    List<Report> getReportsByPatientId(Integer patientId);
    void updateDoctorObservation(Integer reportId, String doctorObservation);
}
