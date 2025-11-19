package jdbcs;
import POJOs.Doctor;

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
 */



public class ManagerJDBC {

    private Connection c = null;
    private PatientJDBC patientJDBC;
    private DoctorJDBC doctorJDBC;
    private ReportJDBC reportJDBC;
    private SymptomJDBC symptomJDBC;

    // CONSTRUCTOR
    public ManagerJDBC() {

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:./db/ParkinsonTelemonitoringDDBB.db");
            c.createStatement().execute("PRAGMA foreign_keys = ON");

            System.out.println("DDBB CONNECTION OPENED!");

            createUserTables();
            createDoctorsTable();
            createPatientsTable();
            createReportsTable();
            createSymptomsTable();

            // Inicializar JDBCs
            patientJDBC = new PatientJDBC(this);
            doctorJDBC = new DoctorJDBC(this);
            reportJDBC = new ReportJDBC(this);
            symptomJDBC = new SymptomJDBC(this);

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

    // ---------------------- TABLAS -----------------------

    private void createUserTables() {
        String sql = "CREATE TABLE IF NOT EXISTS User ("
                + "id VARCHAR(20) PRIMARY KEY, "
                + "email TEXT NOT NULL UNIQUE, "
                + "full_name TEXT NOT NULL, "
                + "password TEXT NOT NULL"
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
                + "doctor_id TEXT PRIMARY KEY, "
                + "full_name TEXT NOT NULL, "
                + "dob TEXT NOT NULL, "
                + "email TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL"
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
                + "patient_id TEXT PRIMARY KEY, "
                + "full_name TEXT NOT NULL, "
                + "dob TEXT NOT NULL, "
                + "email TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "doctor_id TEXT, "
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
                + "report_id TEXT PRIMARY KEY, "
                + "report_date TEXT NOT NULL, "
                + "signalEMG TEXT, "
                + "signalEDA TEXT, "
                + "signalECG TEXT, "
                + "signalACC TEXT, "
                + "patient_id TEXT NOT NULL, "
                + "doctor_id TEXT NOT NULL, "
                + "patient_observation TEXT, "
                + "doctor_observation TEXT, "
                + "FOREIGN KEY (patient_id) REFERENCES Patient(patient_id), "
                + "FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)"
                + ");";

        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla Report creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createSymptomsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Symptom ("
                + "symptom_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL UNIQUE"
                + ");";

        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla Symptom creada correctamente.");
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

    public SymptomJDBC getSymptomJDBC() {
        return symptomJDBC;
    }
}

