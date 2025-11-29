package managers;

import POJOs.Doctor;
import java.util.List;

public interface DoctorManager {
    void addDoctor(Doctor doctor);
    Integer getRandomDoctorId();
    Doctor getDoctorByDoctorId(Integer doctorId);
    Integer getDoctorIdByUserId(Integer userId);
    List<Doctor> readDoctors();
    String getPasswordByDoctorId(Integer doctorId);
}
