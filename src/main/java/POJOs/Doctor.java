package POJOs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Doctor extends User {
    private String password; //TODO: Encriptar;
    private LocalDate dob;
    private Set<Patient> patients;

    public Doctor(String email, String password, String fullName, LocalDate dob, Set<Patient> patients) {
        super(email, fullName);
        this.password = password;
        this.dob = dob;
        this.patients = patients;
    }
}
