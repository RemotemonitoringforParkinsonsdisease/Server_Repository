package jdbcs;

import POJOs.Report;
import POJOs.Symptoms;
import managers.ReportManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides JDBC-based access to report data, including inserting new reports,
 * retrieving reports for a given patient, updating the doctor observation and
 * converting symptoms between list and string formats. It uses the shared
 * ManagerJDBC instance to obtain the database connection and execute SQL
 * queries.
 */
public class ReportJDBC implements ReportManager {

    private ManagerJDBC manager;

    /**
     * Creates a new ReportJDBC helper bound to the given JDBC manager, which
     * will be used to obtain the database connection and execute all report
     * related queries.
     *
     * @param manager the JDBC manager that provides the database connection
     */
    public ReportJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    /**
     * Inserts a new report record into the database using the information
     * stored in the given Report object. The method stores the report date,
     * patient identifier, patient and doctor observations, the list of
     * symptoms converted to a string and the path or name of the signals
     * file. If an SQL error occurs, the exception is printed and the method
     * finishes without throwing it.
     *
     * @param report the report entity containing the data to be stored
     */
    public void addReport(Report report) {
        String sql = "INSERT INTO report (report_date, patient_id, patient_observation, doctor_observation, symptoms_list, signals_file_name) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, report.getReportDate().toString());
            stmt.setInt(2, report.getPatientId());
            stmt.setString(3, report.getPatientObservation());
            stmt.setString(4, report.getDoctorObservation());
            stmt.setString(5, parseSymptoms2(report.getSymptoms()));
            stmt.setString(6,report.getSignalsFilePath());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all report records associated with a given patient identifier
     * and returns them as a list of Report objects. For each row, it builds
     * a Report instance including date, observations, symptoms list and the
     * signals file path. If no reports are found, the method returns an empty
     * list. If an SQL error occurs, the exception is printed and the list
     * accumulated up to that point is returned.
     *
     * @param patientId the identifier of the patient whose reports are requested
     * @return a list with all reports linked to the given patient, possibly empty
     */
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
                String stringSymptoms = rs.getString("symptoms_list");
                String signalsFilePath = rs.getString("signals_file_name");
                List <Symptoms> symptomsList = parseSymptoms(stringSymptoms);

                Report r = new Report(patientId, reportDate, patientObservation, doctorObservation, symptomsList, signalsFilePath);
                r.setReportId(reportId);
                reports.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    /**
     * Converts a comma-separated string of symptom names into a list of
     * Symptoms enum values. If the input string is null or empty, it returns
     * an empty list. The method splits the string by commas and converts each
     * token to the corresponding enum constant.
     *
     * @param symptomsStr the string containing symptom names separated by commas
     * @return a list of Symptoms parsed from the string, possibly empty
     */
    public List<Symptoms> parseSymptoms(String symptomsStr) {
        List<Symptoms> symptoms = new ArrayList<>();

        if (symptomsStr == null || symptomsStr.isEmpty()) {
            return symptoms;
        }

        String[] tokens = symptomsStr.split(",");

        for (String token : tokens) {
            symptoms.add(Symptoms.valueOf(token));
        }

        return symptoms;
    }

    /**
     * Updates the doctor observation field of a report identified by its
     * report identifier. The method sets the new observation text in the
     * database and executes the corresponding update statement. If an SQL
     * error occurs, the exception is printed and the method finishes without
     * throwing it.
     *
     * @param reportId          the identifier of the report to be updated
     * @param doctorObservation the new observation text written by the doctor
     */
    public void updateDoctorObservation(Integer reportId, String doctorObservation) {
        String sql = "UPDATE report SET doctor_observation = ? WHERE report_id = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, doctorObservation);
            stmt.setInt(2, reportId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the report identifier associated with a given signals file
     * path or name. It searches the report table by the signals_file_name
     * field and, if a matching row is found, returns its report_id. If no
     * report is found or an error occurs, the method returns null.
     *
     * @param signalsFilePath the path or name of the signals file stored in the report
     * @return the identifier of the report that matches the file path, or null if not found
     */
    public Integer getReportIdBySignalFilePath(String signalsFilePath) {
        String sql = "SELECT report_id FROM report WHERE signals_file_name = ?";

        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, signalsFilePath);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("report_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Converts a list of Symptoms enum values into a single comma-separated
     * string, using the enum names of each element. This string can later be
     * stored in the database or parsed back using the parseSymptoms method.
     *
     * @param symptoms the list of symptoms to be converted into a string
     * @return a comma-separated string representation of the symptoms list
     */
    public String parseSymptoms2(List<Symptoms> symptoms){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < symptoms.size(); i++) {
            sb.append(symptoms.get(i).name());
            if (i < symptoms.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
