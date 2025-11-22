package POJOs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Patient{
    private String fullName;
    private Integer userId;
    private Integer patientId;
    private Integer doctorId;
    private String patientPassword;
    private LocalDate dob;
    private List<Report> reports;



    public Patient(String fullName, Integer userId, Integer patientId, Integer doctorId, String patientPassword, LocalDate dob) {
         this.fullName = fullName;
        this.userId = userId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientPassword = patientPassword;
        this.dob = dob;
    }
    public Patient(String fullName,Integer userId, Integer patientId, Integer doctorId, String patientPassword, LocalDate dob, List<Report> reports) {
        this.fullName = fullName;
        this.userId = userId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientPassword = patientPassword;
        this.dob = dob;
        this.reports = reports;
    }
    public Patient(String password, String fullName, LocalDate dob) {
        this.fullName = fullName;
        this.dob = dob;
        this.patientPassword = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientPassword() {
        return patientPassword;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
    public void addReport(Report report) {
        if (this.reports == null) {
            this.reports = new ArrayList<>();
        }
        this.reports.add(report);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
