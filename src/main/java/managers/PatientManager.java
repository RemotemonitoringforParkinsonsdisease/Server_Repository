package managers;

import POJOs.Patient;
import java.util.List;

public interface PatientManager {
    void addPatient(Patient patient);
    Patient getPatientByPatientId(Integer patientId);
    List<Patient> readPatients();
    String getPasswordByPatientId(Integer patientId);
    Integer getPatientIdByUserId(Integer userId);
    Patient getPatientByEmail(String email);
    List<Patient> getPatientsByDoctor(Integer doctorId);
    Patient getPatientByUserId(Integer userId);
}

