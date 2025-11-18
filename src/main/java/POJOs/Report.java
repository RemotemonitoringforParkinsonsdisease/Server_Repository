package POJOs;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Report {

    private String reportId;
    private Patient patient;
    private LocalDate reportDate;
    private Set<Signal> signals;
    private List<Symptoms> symptoms;
    private String patientObservation;
    private String doctorObservation;

    // Constructor necesario para ReportJDBC
    public Report(Patient patient, LocalDate reportDate, String patientObservation, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getPatientObservation() {
        return patientObservation;
    }

    public String getDoctorObservation() {
        return doctorObservation;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportId() {
        return reportId;
    }

    public Signal getSignalByType(SignalType type) {
        if (signals == null) return null;
        for (Signal s : signals) {
            if (s.getSignalType() == type) return s;
        }
        return null;
    }
    public LocalDate getReportDate() {
        return reportDate;
    }

}
