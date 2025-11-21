package POJOs;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Report {

    private Integer reportId;
    private Integer patientId;
    private LocalDate reportDate;
    private List<Signal> signals;
    private List<Symptoms> symptoms;
    private String patientObservation;
    private String doctorObservation;

    public Report(Integer reportId, Integer patientId, LocalDate reportDate, String patientObservation) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public List<Signal> getSignals() {
        return signals;
    }

    public void setSignals(List<Signal> signals) {
        this.signals = signals;
    }

    public List<Symptoms> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Symptoms> symptoms) {
        this.symptoms = symptoms;
    }

    public String getPatientObservation() {
        return patientObservation;
    }

    public void setPatientObservation(String patientObservation) {
        this.patientObservation = patientObservation;
    }

    public String getDoctorObservation() {
        return doctorObservation;
    }

    public void setDoctorObservation(String doctorObservation) {
        this.doctorObservation = doctorObservation;
    }

    public Signal getSignalByType(SignalType type) {
        if (signals == null) return null;
        for (Signal s : signals) {
            if (s.getSignalType() == type) return s;
        }
        return null;
    }
}
