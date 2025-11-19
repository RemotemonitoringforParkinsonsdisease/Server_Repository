package managers;

import POJOs.Doctor;
import java.util.List;

public interface DoctorManager {
    void addDoctor(String fullName, String email, String doctorId);
    void addDoctor(Doctor doctor);
    Doctor getDoctorById(String id);
    List<Doctor> readDoctors();
    Doctor getDoctorByEmail(String email);
}
