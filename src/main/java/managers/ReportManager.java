package managers;

import POJOs.Report;
import java.util.List;

public interface ReportManager {
    void addReport(Report report);
    Report getReportById(String reportId);
    List<Report> readReports();
}
