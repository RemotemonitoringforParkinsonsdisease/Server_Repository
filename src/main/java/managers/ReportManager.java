package managers;

import POJOs.Report;
import java.util.List;

public interface ReportManager {
    void addReport(Report report);
    Report getReportById(Integer reportId);
    List<Report> readReports();
    List<Report> getReportsByPatientId(Integer patientId);
    void updateDoctorObservation(Integer reportId, String doctorObservation);
}
