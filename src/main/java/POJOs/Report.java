package POJOs;
import sun.misc.Signal;

import java.time.LocalDate;
import java.util.Set;

public class Report { //Cuando un paciente manda un report al doctor, hay q ver si el doctor se comunica a través del mismo report

    private Integer reportId;
    private Integer patientId;
    private Integer doctorId;
    private Patient patient;
    private LocalDate reportDate;
    private Set<Signal> signals;
    private Set<Symptoms> symptoms;
    private String patientObservation;
    private String doctorObservation;//El texto que le manda el paciente al doctor, podemos hacer booleans como dijo arecha también
    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Symptoms> symptoms, Set<Signal> signals,  String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }

    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Symptoms> symptoms, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.doctorObservation = doctorObservation;

    }
    public Report(Patient patient, LocalDate reportDate, Set<Symptoms> symptoms, Set<Signal> signals, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Signal> signals, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Signal> signals, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Symptoms> symptoms, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Symptoms> symptoms, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.signals = signals;
    }

    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Symptoms> symptoms) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Symptoms> symptoms, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Symptoms> symptoms) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
    } //La señal grabada por el bitalino del paciente, supongo que seran varios canales, puede ser un Set / List de String, hay que mirar tipo de datos del Bitalino
    private boolean isSeen; //Ha sido visto por doctor, alomejor habría que implementar si ha sido respondido

    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Symptoms> symptoms, Set<Signal> signals,  String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }

    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Symptoms> symptoms, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.doctorObservation = doctorObservation;

    }
    public Report(Patient patient, LocalDate reportDate, Set<Symptoms> symptoms, Set<Signal> signals, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Signal> signals, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Signal> signals, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Symptoms> symptoms, String doctorObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
        this.doctorObservation = doctorObservation;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Symptoms> symptoms, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.signals = signals;
    }

    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Symptoms> symptoms) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Symptoms> symptoms, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, String patientObservation) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Signal> signals) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.signals = signals;
    }
    public Report(Patient patient, LocalDate reportDate, Set<Symptoms> symptoms) {
        this.patient = patient;
        this.reportDate = reportDate;
        this.symptoms = symptoms;
    }
    public Integer getReportId() {
        return reportId;
    }

    public boolean getIsSeen() {
        return isSeen;
    }
    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }
}
