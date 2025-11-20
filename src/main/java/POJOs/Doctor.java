package POJOs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class Doctor extends User {
    private String fullName;
    private LocalDate dob;
    private List<Patient> patients;

    public Doctor(String fullName, String fullName, LocalDate dob, List<Patient> patients) {
        this.fullName = fullName;
        this.dob = dob;
        this.patients = patients;
    }
    public Doctor(String id, String fullName, LocalDate dob) {
        this.id = id;
        this.fullName = fullName;
        this.dob = dob;
    }
    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public Integer getId() {
        return id;
    }

}
