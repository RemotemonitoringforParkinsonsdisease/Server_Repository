package POJOs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Patient extends User {
    private Doctor doctor;
    private LocalDate dob;
    private Set<Report> reports = new HashSet<>();
    private String doctorId;
    private String fullName;


    // Constructor para cargar paciente desde DB
    public Patient(String id, String fullName, LocalDate dob, Doctor doctor) {
        this.id = id;
        this.dob = dob;
        this.doctor = doctor;
        this.fullName = fullName;
    }
    public Patient(String fullName, LocalDate dob) {
        this.dob = dob;
        this.fullName =fullName;
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

    public Integer getId() {
        return id; // id es String con formato "p123456789"
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }


}
