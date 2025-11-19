package POJOs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class Doctor extends User {
    private String password; //TODO: Encriptar;
    private LocalDate dob;
    private List<Patient> patients;

    public Doctor(String email, String password, String fullName, LocalDate dob, List<Patient> patients) {
        super(email, fullName);
        this.password = password;
        this.dob = dob;
        this.patients = patients;
    }
    public Doctor(String id, String fullName, LocalDate dob, String email, String password) {
        super(id, fullName, email);
        this.password = password;
        this.dob = dob;
    }
    public LocalDate getDob() {
        return dob;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id; // id es String con formato "p123456789"
    }

}
