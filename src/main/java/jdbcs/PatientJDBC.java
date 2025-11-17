package jdbcs;

import POJOs.Patient;
import managers.PatientManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class PatientJDBC implements PatientManager {

    private ManagerJDBC manager;
    public PatientJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    public void addPatient(String fullName, LocalDate dob, String email, int doctor_id, int patient_id) {
        String sql= "INSERT INTO Patient (fullName, dob, email, doctor_id, user_id) VALUES (?,?,?,?,?);";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, fullName);
            String date = dob.toString();
            p.setString(2, date);
            p.setString(3, email);
            p.setInt(4, doctor_id);
            p.setInt(5, patient_id);
            p.executeUpdate();
            p.close();
        }catch(SQLException e ) {
            e.printStackTrace();
        }
    }

    public ArrayList<Patient> readPatients() {
        ArrayList<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient;";

        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String fullName = rs.getString("fullName");
                String d = rs.getString("dob");
                LocalDate dob = Utilities.stringToDate(d);
                String email = rs.getString("email");
                Patient p = new Patient(id, fullName, dob, email);

                patients.add(p);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;

    }


}


