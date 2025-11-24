package jdbcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {

    public static void main(String[] args) {
        // Obtiene la instancia del ManagerJDBC
        ManagerJDBC managerJDBC = ManagerJDBC.getInstance();

        // Obtiene la conexión
        try (Connection conn = managerJDBC.getConnection()) {
            // Llama al método para crear las tablas
            managerJDBC.createTables(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
