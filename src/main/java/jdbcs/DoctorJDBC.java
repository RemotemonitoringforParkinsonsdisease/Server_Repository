package jdbcs;

import POJOs.Doctor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import managers.DoctorManager;

public class DoctorJDBC implements DoctorManager {

    private ManagerJDBC manager;
    public DoctorJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }


    public void addDoctor(String fullName, LocalDate dob, String email, int doctor_id) {
        String sql= "INSERT INTO Doctor (fullName, dob, email, doctor_id) VALUES (?,?,?,?);";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, fullName);
            String date = dob.toString();
            p.setString(2, date);
            p.setString(3, email);
            p.setString(4,doctor_id );
            p.executeUpdate();
            p.close();
        }catch(SQLException e ) {
            e.printStackTrace();
        }
    }

}
