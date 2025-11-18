package jdbcs;

import managers.ReportManager;
import POJOs.Report;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReportJDBC implements ReportManager {

    private ManagerJDBC manager;
    public ReportManagerJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    public void addReport(Report report) {
        String sql = "INSERT INTO Report (date, report, signalEMG, signalEDA, signalECG, signalACC, patient_id, doctor_id, observation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {

            PreparedStatement statement = manager.getConnection().prepareStatement(sql);

            statement.setString(1, report.getReportDate().toString());
            statement.setString(2, report.getReportId());
            statement.setString(3, report.getSignalEMG().valuesToString());
            statement.setString(4, report.getSignalEDA().valuesToString());
            statement.setString(5, report.getSignalECG().valuesToString());
            statement.setString(6, report.getSignalACC().valuesToString());
            statement.setInt(7, report.getPatient_id());
            statement.setInt(8, report.getDoctor_id());
            statement.setString(9, report.getObservation());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
