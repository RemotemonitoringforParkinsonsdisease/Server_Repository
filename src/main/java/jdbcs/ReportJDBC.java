package jdbcs;

import POJOs.Report;
import POJOs.SignalType;
import managers.ReportManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportJDBC implements ReportManager {

    private ManagerJDBC manager;

    public ReportJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    @Override
    public void addReport(Report report) {
        String sql = "INSERT INTO Report (report_id, report_date, signalEMG, signalEDA, signalECG, signalACC, patient_id, doctor_id, patient_observation, doctor_observation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, report.getReportId());
            stmt.setString(2, report.getReportDate().toString());

            // Guardamos cada tipo de señal si existe
            stmt.setString(3, report.getSignalByType(SignalType.EMG) != null ? report.getSignalByType(SignalType.EMG).intValuesToString() : "");
            stmt.setString(4, report.getSignalByType(SignalType.EDA) != null ? report.getSignalByType(SignalType.EDA).intValuesToString() : "");
            stmt.setString(5, report.getSignalByType(SignalType.ECG) != null ? report.getSignalByType(SignalType.ECG).intValuesToString() : "");
            stmt.setString(6, report.getSignalByType(SignalType.ACC) != null ? report.getSignalByType(SignalType.ACC).intValuesToString() : "");

            // IDs como Strings
            stmt.setString(7, report.getPatient().getId());
            stmt.setString(8, report.getPatient().getDoctor() != null ? report.getPatient().getDoctor().getId() : null);

            stmt.setString(9, report.getPatientObservation());
            stmt.setString(10, report.getDoctorObservation());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Report getReportById(String reportId) {
        String sql = "SELECT * FROM Report WHERE report_id = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, reportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Report r = new Report(null, null, rs.getString("patient_observation"), rs.getString("doctor_observation"));
                r.setReportId(rs.getString("report_id"));
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Report> readReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM Report";
        try (Statement stmt = manager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Report r = new Report(null, null, rs.getString("patient_observation"), rs.getString("doctor_observation"));
                r.setReportId(rs.getString("report_id"));
                reports.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public List<Report> getReportsByPatient(String patientId) {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM Report WHERE patient_id = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String reportId = rs.getString("report_id");
                String patientObservation = rs.getString("patient_observation");
                String doctorObservation = rs.getString("doctor_observation");

                Report r = new Report(null, null, patientObservation, doctorObservation);
                r.setReportId(reportId);
                reports.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
}
