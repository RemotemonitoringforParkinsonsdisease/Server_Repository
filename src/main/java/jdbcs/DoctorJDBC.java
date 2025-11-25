package jdbcs;

import POJOs.Doctor;
import POJOs.Patient;
import managers.DoctorManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DoctorJDBC implements DoctorManager {

    private ManagerJDBC manager;

    // Constructor
    public DoctorJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    // Método para agregar un doctor (mejorado para manejar auto incremento de doctor_id)
    public void addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctor (user_id, doctor_password, dob, full_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, doctor.getUserId());
            stmt.setString(2, doctor.getDoctorPassword());
            stmt.setString(3, doctor.getDob().toString());
            stmt.setString(4, doctor.getFullName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener un doctor aleatorio
    public Integer getRandomDoctorId() {
        List<Doctor> doctors = this.readDoctors(); // Método para obtener todos los doctores
        if (doctors != null && !doctors.isEmpty()) {
            Random rand = new Random();
            return doctors.get(rand.nextInt(doctors.size())).getDoctorId(); // Devuelve un doctor aleatorio
        }
        return null; // Si no hay doctores, retorna null
    }



    // Método para obtener un doctor por su ID
    public Doctor getDoctorByDoctorId(Integer id) {
        String sql = "SELECT * FROM doctor WHERE doctor_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Integer doctorId = rs.getInt("doctor_id");
                String doctorPassword = rs.getString("doctor_password");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String fullName = rs.getString("full_name");

                return new Doctor(userId, doctorId, doctorPassword, dob, null, fullName); // null for patients, can be fetched separately if needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getDoctorIdByUserId(Integer userId) {
        String sql = "SELECT doctor_id FROM doctor WHERE user_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId); // Establecer el user_id en la consulta SQL
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("doctor_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el doctor, se devuelve null
    }


    // Método para obtener todos los doctores
    public List<Doctor> readDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctor";
        try (Statement stmt = manager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Integer doctorId = rs.getInt("doctor_id");
                String doctorPassword = rs.getString("doctor_password");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String fullName = rs.getString("full_name");

                doctors.add(new Doctor(userId, doctorId, doctorPassword, dob, null, fullName)); // null for patients, can be fetched separately if needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Método para obtener un doctor por su nombre completo
    public Doctor getDoctorByFullName(String fullName) {
        String sql = "SELECT * FROM doctor WHERE full_name = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, fullName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Integer doctorId = rs.getInt("doctor_id");
                String doctorPassword = rs.getString("doctor_password");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String fullNameDb = rs.getString("full_name");

                return new Doctor(userId, doctorId, doctorPassword, dob, null, fullName); // null for patients, can be fetched separately if needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obtener la contraseña del doctor por su doctor_id
    public String getPasswordByDoctorId(Integer doctorId) {
        String sql = "SELECT doctor_password FROM doctor WHERE doctor_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, doctorId); // Establecer el doctor_id en la consulta SQL
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("doctor_password");  // Devolver la contraseña del doctor
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra la contraseña, se devuelve null
    }

    // Método para obtener un doctor por su contraseña
    public Doctor getDoctorByPassword(String password) {
        String sql = "SELECT * FROM doctor WHERE doctor_password = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Integer doctorId = rs.getInt("doctor_id");
                String doctorPassword = rs.getString("doctor_password");
                LocalDate dob = LocalDate.parse(rs.getString("dob"));
                String fullName = rs.getString("full_name");

                return new Doctor(userId, doctorId, doctorPassword, dob, null, fullName); // null for patients, can be fetched separately if needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
