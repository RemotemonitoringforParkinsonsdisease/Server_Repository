package jdbcs;

import POJOs.Report;
import POJOs.SignalType;
import POJOs.Symptoms;
import managers.ReportManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportJDBC implements ReportManager {

    private ManagerJDBC manager;

    // Constructor
    public ReportJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    // Método para agregar un reporte
    public void addReport(Report report) {
        String sql = "INSERT INTO report (report_id, report_date, patient_id, patient_observation, doctor_observation) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, report.getReportId());
            stmt.setString(2, report.getReportDate().toString());
            stmt.setInt(3, report.getPatientId());
            stmt.setString(4, report.getPatientObservation());
            stmt.setString(5, report.getDoctorObservation());
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

    // Método para obtener los reports de un paciente específico
    public List<Report> getReportsByPatientId(Integer patientId) {
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
                String stringSymptoms = rs.getString("symptoms");
                String signalsFilePath = rs.getString("signals_file_name");
                List <Symptoms> symptomsList = parseSymptoms(stringSymptoms);

                //TODO passar symptoms a lista
                Report r = new Report(patientId, reportDate, patientObservation, doctorObservation, symptomsList, signalsFilePath);
                r.setReportId(reportId);
                reports.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    //TODO mirar donde metemos este método
    public List<Symptoms> parseSymptoms(String symptomsStr) {
        List<Symptoms> symptoms = new ArrayList<>();

        if (symptomsStr == null || symptomsStr.isEmpty()) {
            return symptoms; // lista vacía
        }

        String[] tokens = symptomsStr.split(",");

        for (String token : tokens) {
            symptoms.add(Symptoms.valueOf(token));
        }

        return symptoms;
    }

    // Método para actualizar la observación del doctor en un reporte
    public void updateDoctorObservation(Integer reportId, String doctorObservation) {
        String sql = "UPDATE report SET doctor_observation = ? WHERE report_id = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, doctorObservation);  // Establecer la nueva observación del doctor
            stmt.setInt(2, reportId);  // Establecer el report_id en la consulta SQL
            stmt.executeUpdate();  // Ejecutar la actualización
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getReportIdBySignalFilePath(String signalsFilePath) {
        String sql = "SELECT report_id FROM report WHERE signal_file_name = ?";

        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, signalsFilePath);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("report_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // No encontrado
    }
}
