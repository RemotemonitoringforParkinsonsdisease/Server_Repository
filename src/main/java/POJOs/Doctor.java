package POJOs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class Doctor{
    private String fullName;
    private Integer userId;
    private Integer doctorId;
    private String doctorPassword;
    private LocalDate dob;
    private List<Patient> patients;

    public Doctor(Integer userId, Integer doctorId, String doctorPassword, LocalDate dob) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
    }
    public Doctor(String fullname, String doctorPassword, LocalDate dob) {
        this.fullName = fullname;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
    }

    public Doctor(Integer userId, Integer doctorId, String doctorPassword, LocalDate dob, List<Patient> patients, String fullName) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
        this.patients = patients;
        this.fullName = fullName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "fullName='" + fullName + '\'' +
                ", userId=" + userId +
                ", doctorId=" + doctorId +
                ", doctorPassword='" + doctorPassword + '\'' +
                ", dob=" + dob +
                '}';
    }
}
