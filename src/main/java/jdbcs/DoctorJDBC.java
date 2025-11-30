package jdbcs;

import POJOs.Doctor;
import managers.DoctorManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Provides JDBC-based access to doctor data, including inserting new doctors,
 * retrieving doctor records and passwords, and selecting a random doctor from
 * the database. It uses the shared ManagerJDBC instance to obtain the database
 * connection and execute SQL queries.
 */
public class DoctorJDBC implements DoctorManager {

    private ManagerJDBC manager;

    /**
     * Creates a new DoctorJDBC helper bound to the given JDBC manager, which
     * will be used to obtain the database connection and execute all doctor
     * related queries.
     *
     * @param manager the JDBC manager that provides the database connection
     */
    public DoctorJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    /**
     * Inserts a new doctor record into the database using the information
     * stored in the given Doctor object. The method stores the user identifier,
     * password, date of birth and full name of the doctor. If an SQL error
     * occurs, the exception is printed and the method finishes without throwing it.
     *
     * @param doctor the doctor entity containing the data to be stored
     */
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

    /**
     * Selects a random doctor from the database and returns its identifier.
     * The method retrieves the full list of doctors and, if the list is not
     * empty, picks one at random and returns its doctor id. If there are no
     * doctors available or an error occurs, it returns null.
     *
     * @return the identifier of a random doctor, or null if no doctors are found
     */
    public Integer getRandomDoctorId() {
        List<Doctor> doctors = this.readDoctors(); // Metodo para obtener todos los doctores
        if (doctors != null && !doctors.isEmpty()) {
            Random rand = new Random();
            return doctors.get(rand.nextInt(doctors.size())).getDoctorId(); // Devuelve un doctor aleatorio
        }
        return null; // Si no hay doctores, retorna null
    }

    /**
     * Retrieves a doctor record from the database using its doctor identifier.
     * The method queries the doctor table, builds a Doctor object with the
     * stored values for that id and returns it. If no doctor is found with
     * the given identifier or an error occurs, it returns null.
     *
     * @param id the identifier of the doctor to look up
     * @return the Doctor object for the given id, or null if it cannot be found
     */
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

                return new Doctor(userId, doctorId, doctorPassword, dob, null, fullName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the doctor identifier associated with the given user identifier.
     * It searches the doctor table for a row whose user_id matches the provided
     * value and, if found, returns the corresponding doctor_id. If no doctor is
     * linked to that user or an error occurs, the method returns null.
     *
     * @param userId the user identifier linked to the doctor
     * @return the doctor id for the given user, or null if it cannot be found
     */
    public Integer getDoctorIdByUserId(Integer userId) {
        String sql = "SELECT doctor_id FROM doctor WHERE user_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("doctor_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all doctor records stored in the database and returns them as
     * a list of Doctor objects. If no doctors are found, the method returns an
     * empty list. If an SQL error occurs, the exception is printed and the list
     * accumulated up to that point is returned.
     *
     * @return a list with all doctors stored in the database, possibly empty
     */
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

                doctors.add(new Doctor(userId, doctorId, doctorPassword, dob, null, fullName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    /**
     * Retrieves the password of a doctor given its doctor identifier. The method
     * queries the doctor table and returns the stored password for that id. If
     * no doctor is found or an error occurs, it returns null.
     *
     * @param doctorId the identifier of the doctor whose password is requested
     * @return the password of the doctor, or null if it cannot be found
     */
    public String getPasswordByDoctorId(Integer doctorId) {
        String sql = "SELECT doctor_password FROM doctor WHERE doctor_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("doctor_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
