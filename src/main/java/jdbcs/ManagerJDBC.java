package jdbcs;

import java.io.File;
import java.sql.*;

public class ManagerJDBC {

    private static ManagerJDBC instance;
    private PatientJDBC patientJDBC;
    private DoctorJDBC doctorJDBC;
    private ReportJDBC reportJDBC;
    private UserJDBC userJDBC;
    private AdminJDBC adminJDBC;

    /**
     * Initializes the JDBC manager, loading the SQLite driver, creating the
     * database folder if it does not exist, opening the database file and
     * ensuring that all required tables are created. It also constructs the
     * helper objects for user, doctor, patient, report and admin access.
     * If any error occurs during initialization, a runtime exception is thrown.
     */
    public ManagerJDBC() {
        try {
            Class.forName("org.sqlite.JDBC");

            File dbDirectory = new File("./database");
            if (!dbDirectory.exists()) {
                dbDirectory.mkdirs();
            }

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./database/parkinson.db")) {
                conn.createStatement().execute("PRAGMA foreign_keys = ON");
                createTables(conn);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing DB", e);
        }
        userJDBC = new UserJDBC(this);
        doctorJDBC = new DoctorJDBC(this);
        patientJDBC = new PatientJDBC(this);
        reportJDBC = new ReportJDBC(this);
        adminJDBC = new AdminJDBC(this);
    }

    /**
     * Returns the singleton instance of the JDBC manager. If it has not been
     * created yet, this method instantiates it before returning it. This
     * ensures that the database is initialized only once in the application.
     *
     * @return the shared ManagerJDBC instance
     */
    public static synchronized ManagerJDBC getInstance() {
        if (instance == null) {
            instance = new ManagerJDBC();
        }
        return instance;
    }

    /**
     * Obtains a new connection to the SQLite database file used by the
     * application. If the connection cannot be created, a runtime exception
     * is thrown wrapping the original SQL error.
     *
     * @return a new JDBC connection to the Parkinson database
     */
    public Connection getConnection(){
        try{
            return DriverManager.getConnection("jdbc:sqlite:./database/parkinson.db");
        }catch(SQLException e){
            throw new RuntimeException("Can't get connection", e);
        }
    }

    /**
     * Creates all the database tables required by the application if they do
     * not already exist. It uses the provided connection to execute the DDL
     * statements for user, doctor, patient, report and admin tables, enabling
     * foreign key support beforehand. Any SQL exception is caught and ignored.
     *
     * @param conn the open database connection used to create the tables
     */
    public void createTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {

            // Crear tabla de usuario
            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS user (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE NOT NULL
            );
        """);

            // Crear tabla de doctor
            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS doctor (
                doctor_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                full_name TEXT NOT NULL,
                doctor_password TEXT NOT NULL,
                dob TEXT,
                FOREIGN KEY (user_id) REFERENCES user(user_id)
            );
        """);

            // Crear tabla de paciente
            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS patient (
                patient_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                doctor_id INTEGER,
                full_name TEXT NOT NULL,
                patient_password TEXT NOT NULL,
                dob TEXT,
                FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id),
                FOREIGN KEY (user_id) REFERENCES user(user_id)
            );
        """);

            // Crear tabla de reports
            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS report (
                report_id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER NOT NULL,
                report_date TEXT NOT NULL,
                patient_observation TEXT,
                doctor_observation TEXT,
                symptoms_list TEXT,
                signals_file_name TEXT,
                FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
            );
        """);

            // Crear tabla de admin
            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS admin (
                admin_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                admin_password TEXT NOT NULL,
                FOREIGN KEY (user_id) REFERENCES user(user_id)
            );
        """);
        } catch (SQLException e) {
        }
    }

    /**
     * Returns the helper object that provides JDBC operations related to
     * patients, such as inserting and retrieving patient records.
     *
     * @return the PatientJDBC helper associated with this manager
     */
    public PatientJDBC getPatientJDBC() {
        return patientJDBC;
    }

    /**
     * Returns the helper object that provides JDBC operations related to
     * doctors, such as inserting and retrieving doctor records.
     *
     * @return the DoctorJDBC helper associated with this manager
     */
    public DoctorJDBC getDoctorJDBC() {
        return doctorJDBC;
    }

    /**
     * Returns the helper object that provides JDBC operations related to
     * reports, such as inserting and retrieving clinical report records.
     *
     * @return the ReportJDBC helper associated with this manager
     */
    public ReportJDBC getReportJDBC() {
        return reportJDBC;
    }

    /**
     * Returns the helper object that provides JDBC operations related to
     * users, including storing and querying user accounts and their emails.
     *
     * @return the UserJDBC helper associated with this manager
     */
    public UserJDBC getUserJDBC() {
        return userJDBC;
    }

    /**
     * Returns the helper object that provides JDBC operations related to
     * administrators, including inserting new admins and retrieving their
     * identifiers and passwords.
     *
     * @return the AdminJDBC helper associated with this manager
     */
    public AdminJDBC getAdminJDBC() {
        return adminJDBC;
    }
}
