package JDBCs;

import java.sql.*;

/**
 * The {@code ManagerJDBC} class manages the connection and initialization of the SQLite database used in the Parkinson Telemonitoring system.
 *  When created, it connects to {@code ./db/ParkinsonTelemonitoringDDBB.db}, creates all required tables if they do not exist,
 *  and inserts default values such as user roles, Parkinson symptoms, and an administrator account.
 *  <p>
 *  It also provides access to the active {@link Connection} for other JDBC classes
 *  and includes utility methods to close or clear the database when needed.
 */
public class ManagerJDBC {
    private Connection c = null;

    //CONSTRUCTOR
    public ManagerJDBC() {
        try {
            //Open DDBB connection<
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:./db/ParkinsonTelemonitoringDDBB.db");
            c.createStatement().execute("PRAGMA foreign_keys=ON");
            System.out.println("DDBB CONNECTION OPENED!");
            this.createUserTables();
            this.insertValuesIntoRoleTable();
            this.insertValuesIntoSymptomsTable();
            this.insertAdministrator();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return c;
    }

    public void disconnect() {
        try {
            c.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void createUserTables() throws SQLException{


        String sql = "CREATE TABLE IF NOT EXISTS Users ("
                + "id VARCHAR(20) PRIMARY KEY, "
                + "email VARCHAR(255) NOT NULL UNIQUE, "
                + "full_name VARCHAR(255) NOT NULL, "
                + "password VARCHAR(255) NOT NULL"
                + ");";

        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla users creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createPatientsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS patients (" +
                "patient_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "full_name VARCHAR(50) NOT NULL, " +
                "dob DATE NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL" +
                "doctor_id INT, " +
                "FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id))";

        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla patients creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDoctorsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS patients (" +
                "doctor_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "full_name VARCHAR(50) NOT NULL, " +
                "dob DATE NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL" +
                "doctor_id INT))";

        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla doctors creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void insertValuesIntoRoleTable() {

    }
    private void insertValuesIntoSymptomsTable() {

    }
    private void insertAdministrator() {

    }
}