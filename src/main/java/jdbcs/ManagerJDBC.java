
/*
package jdbcs;

import POJOs.Doctor;
import POJOs.SignalType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

/**
 * The {@code ManagerJDBC} class manages the connection and initialization of the SQLite database used in the Parkinson Telemonitoring system.
 *  When created, it connects to {@code ./db/ParkinsonTelemonitoringDDBB.db}, creates all required tables if they do not exist,
 *  and inserts default values such as user roles, Parkinson symptoms, and an administrator account.
 *  <p>
 *  It also provides access to the active {@link Connection} for other JDBC classes
 *  and includes utility methods to close or clear the database when needed.




public class ManagerJDBC {

    private Connection c = null;
    private PatientJDBC patientJDBC;
    private DoctorJDBC doctorJDBC;
    private ReportJDBC reportJDBC;
    private UserJDBC userJDBC;
    private AdminJDBC adminJDBC;
    private SignalJDBC signalJDBC;

    // CONSTRUCTOR
    public ManagerJDBC() {

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:./db/ParkinsonTelemonitoringDDBB.db");
            c.createStatement().execute("PRAGMA foreign_keys = ON");

            System.out.println("DDBB CONNECTION OPENED!");

            createDoctorsTable();
            createPatientsTable();
            createReportsTable();
            createUsersTable();
            createAdminsTable();
            createSignalsTable();

            // Inicializar JDBCs
            patientJDBC = new PatientJDBC(this);
            doctorJDBC = new DoctorJDBC(this);
            reportJDBC = new ReportJDBC(this);
            userJDBC = new UserJDBC(this);
            adminJDBC = new AdminJDBC(this);
            signalJDBC = new SignalJDBC(this);

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
            e.printStackTrace();
        }
    }
    public void createUsersTable(){
        String sql = "CREATE TABLE IF NOT EXISTS User ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "email TEXT UNIQUE NOT NULL"
                + ");";
        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla User creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createAdminsTable(){
        String sql = "CREATE TABLE IF NOT EXISTS Admin ("
                + "admin_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER,"
                + "admin_password TEXT NOT NULL, "
                + "FOREIGN KEY (user_id) REFERENCES User (user_id)"
                + ");";
        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla User creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createDoctorsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Doctor ("
                + "doctor_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER,"
                + "full_name TEXT NOT NULL, "
                + "doctor_password TEXT NOT NULL, "
                + "dob TEXT NOT NULL,"
                + "FOREIGN KEY (user_id) REFERENCES User (user_id)"
                + ");";
        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla Doctor creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createPatientsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Patient ("
                + "patient_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER, "
                + "doctor_id INTEGER, "
                + "full_name TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "dob TEXT NOT NULL, "
                + "FOREIGN KEY (user_id) REFERENCES User (user_id),"
                + "FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)"
                + ");";

        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla Patient creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createReportsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Report ("
                + "report_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "patient_id INTEGER NOT NULL, "
                + "report_date TEXT NOT NULL, "
                + "patient_observation TEXT, "
                + "doctor_observation TEXT, "
                + "symptoms TEXT, "
                + "FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE"
                + ");";
        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla Report creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createSignalsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Signal ("
                + "signal_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "report_id INTEGER NOT NULL, "
                + "signal_type TEXT NOT NULL, "
                + "signal_values TEXT NOT NULL, "
                + "FOREIGN KEY (report_id) REFERENCES Report(report_id) ON DELETE CASCADE"
                + ");";
        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla Report creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Doctor assignRandomDoctor() {
        List<Doctor> doctors = doctorJDBC.readDoctors();

        if (doctors == null || doctors.isEmpty()) {
            return null; // NO hay doctores disponibles
        }

        Random rand = new Random();
        return doctors.get(rand.nextInt(doctors.size()));
    }


    // ---------------- GETTERS JDBC -------------------

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
*/
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
                values TEXT,  -- Se guardan los valores de la señal como texto
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