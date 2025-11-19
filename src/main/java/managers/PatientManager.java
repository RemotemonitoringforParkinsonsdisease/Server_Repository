package managers;

import POJOs.Patient;
import java.util.List;

public interface PatientManager {
    Patient getPatientByEmail(String email);
    Patient getPatientById(String id);
    List<Patient> readPatients();
    void addPatient(Patient patient);
    List<Patient> getPatientsByDoctor(String doctorId);
}
