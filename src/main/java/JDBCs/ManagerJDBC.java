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
            this.createTables();
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

    private void createTables() throws SQLException{

        Statement stmt = c.createStatement();

        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users ("
                + "id VARCHAR(20) PRIMARY KEY, "
                + "email VARCHAR(255) NOT NULL UNIQUE, "
                + "full_name VARCHAR(255) NOT NULL, "
                + "password VARCHAR(255) NOT NULL"
                + ");";
        stmt.executeUpdate(createUsersTable);

    }

    private void insertValuesIntoRoleTable() {

    }
    private void insertValuesIntoSymptomsTable() {

    }
    private void insertAdministrator() {

    }
}