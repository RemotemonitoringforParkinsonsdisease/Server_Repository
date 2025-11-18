package managers;

import POJOs.Patient;
import java.util.List;

public interface PatientManager {
    void addPatient(String fullName, String email, int doctorId, int patientId);
    Patient getPatientById(int id);
    List<Patient> readPatients();
}
