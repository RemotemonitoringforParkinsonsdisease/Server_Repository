package jdbcs;

import POJOs.Report;
import POJOs.SignalType;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportJDBC {

    private ManagerJDBC manager;

    // Constructor
    public ReportJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    // Método para agregar un reporte
    public void addReport(Report report) {
        String sql = "INSERT INTO report (report_id, report_date, patient_id, doctor_id, patient_observation, doctor_observation) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, report.getReportId());
            stmt.setString(2, report.getReportDate().toString());
            stmt.setInt(3, report.getPatientId());
            stmt.setInt(4, report.getPatientId()); // assuming doctor_id is the same as patient_id for this example (update as needed)
            stmt.setString(5, report.getPatientObservation());
            stmt.setString(6, report.getDoctorObservation());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener un reporte por su ID
    public Report getReportById(Integer reportId) {
        String sql = "SELECT * FROM report WHERE report_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer patientId = rs.getInt("patient_id");
                LocalDate reportDate = LocalDate.parse(rs.getString("report_date"));
                String patientObservation = rs.getString("patient_observation");
                String doctorObservation = rs.getString("doctor_observation");

                Report r = new Report(patientId, reportDate, patientObservation, doctorObservation, null, null);
                r.setReportId(reportId);
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obtener todos los reportes
    public List<Report> readReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM report";
        try (Statement stmt = manager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer reportId = rs.getInt("report_id");
                Integer patientId = rs.getInt("patient_id");
                LocalDate reportDate = LocalDate.parse(rs.getString("report_date"));
                String patientObservation = rs.getString("patient_observation");
                String doctorObservation = rs.getString("doctor_observation");

                Report r = new Report(patientId, reportDate, patientObservation, doctorObservation, null, null);
                r.setReportId(reportId);
                reports.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    // Método para obtener los reportes de un paciente específico
    public List<Report> getReportsByPatient(Integer patientId) {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM report WHERE patient_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer reportId = rs.getInt("report_id");
                LocalDate reportDate = LocalDate.parse(rs.getString("report_date"));
                String patientObservation = rs.getString("patient_observation");
                String doctorObservation = rs.getString("doctor_observation");

                Report r = new Report(patientId, reportDate, patientObservation, doctorObservation, null, null);
                r.setReportId(reportId);
                reports.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

}
