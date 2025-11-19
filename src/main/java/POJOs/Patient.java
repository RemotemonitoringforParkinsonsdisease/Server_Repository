package POJOs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Patient extends User {
    private String password;
    private Doctor doctor;
    private LocalDate dob;
    private Set<Report> reports = new HashSet<>();
    private String doctorId;


    // Constructor para cargar paciente desde DB
    public Patient(String id, String fullName, LocalDate dob, String email, Doctor doctor) {
        super(email, fullName);
        this.id = id;
        this.dob = dob;
        this.doctor = doctor;
    }
    public Patient(String email, String fullName, LocalDate dob, String password, String letter) {
        super(email, fullName, letter);
        this.dob = dob;
        this.password = password;
    }

    /* Constructor para registrar paciente
    public Patient(String email, String fullName, LocalDate dob, String password, Doctor doctor) {
        super(email, fullName, "P"); // letra inicial P para pacientes
        this.dob = dob;
        this.password = password;
        this.doctor = doctor;
    }*/

    // Getters y setters
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public String getId() {
        return id; // id es String con formato "p123456789"
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }


}
