package managers;

import POJOs.Patient;
import java.util.List;

public interface PatientManager {
    void addPatient(Patient patient);
    Patient getPatientByPatientId(Integer patientId);
    String getPasswordByPatientId(Integer patientId);
    Integer getPatientIdByUserId(Integer userId);

}

