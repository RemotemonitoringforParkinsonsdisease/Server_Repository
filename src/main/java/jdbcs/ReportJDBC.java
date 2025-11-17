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
        String sql = "INSERT INTO Interpretation (date, interpretation, signalEMG, signalEDA, patient_id, doctor_id, observation) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try {

            PreparedStatement statement = manager.getConnection().prepareStatement(sql);

            statement.setString(1, report.getDate().toString());
            statement.setString(2, report.getInterpretation());
            statement.setString(3, report.getSignalEMG().valuesToString());
            statement.setString(4, report.getSignalEDA().valuesToString());
            statement.setInt(5, report.getPatient_id());
            statement.setInt(6, report.getDoctor_id());
            statement.setString(7, report.getObservation());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
