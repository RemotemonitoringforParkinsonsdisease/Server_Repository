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
    private SignalJDBC signalJDBC;

    public ManagerJDBC() {
        try {
            Class.forName("org.sqlite.JDBC");

            //Create database folder if it does not exist
            File dbDirectory = new File("./database");
            if (!dbDirectory.exists()) {
                dbDirectory.mkdirs();
            }

            //Create the database only once, not every time we connect
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./database/parkinson.db")) {
                conn.createStatement().execute("PRAGMA foreign_keys = ON");
                createTables(conn);
            }

            System.out.println("Parkinson database correctly initialized\n");

        } catch (Exception e) {
            throw new RuntimeException("Error initializing DB", e);
        }
        userJDBC = new UserJDBC(this);
        doctorJDBC = new DoctorJDBC(this);
        patientJDBC = new PatientJDBC(this);
        reportJDBC = new ReportJDBC(this);
        adminJDBC = new AdminJDBC(this);
        signalJDBC = new SignalJDBC(this);
    }

    public static synchronized ManagerJDBC getInstance() {
        if (instance == null) {
            instance = new ManagerJDBC();
        }
        return instance;
    }

    public Connection getConnection(){
        try{
            return DriverManager.getConnection("jdbc:sqlite:./database/parkinson.db");
        }catch(SQLException e){
            throw new RuntimeException("Can't get connection", e);
        }
    }



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
                FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
            );
        """);

            // Crear tabla de signal
            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS signal (
                signal_id INTEGER PRIMARY KEY AUTOINCREMENT,
                report_id INTEGER NOT NULL,
                signal_type TEXT NOT NULL,
                signal_values TEXT,  -- Se guardan los valores de la señal como texto
                sampling_rate INTEGER,
                FOREIGN KEY (report_id) REFERENCES report(report_id)
            );
        """);

            System.out.println("Tablas creadas correctamente o ya verificadas");

        } catch (SQLException e) {

        }
    }
    public PatientJDBC getPatientJDBC() {
        return patientJDBC;
    }

    public DoctorJDBC getDoctorJDBC() {
        return doctorJDBC;
    }

    public ReportJDBC getReportJDBC() {
        return reportJDBC;
    }

    public UserJDBC getUserJDBC() {
        return userJDBC;
    }
    public AdminJDBC getAdminJDBC() {
        return adminJDBC;
    }
}