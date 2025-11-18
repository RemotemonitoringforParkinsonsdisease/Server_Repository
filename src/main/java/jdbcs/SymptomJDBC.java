package jdbcs;

import POJOs.Symptoms;
import managers.SymptomManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SymptomJDBC implements SymptomManager {

    private ManagerJDBC manager;

    public SymptomJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    @Override
    public void addSymptom(Symptoms symptom) {
        String sql = "INSERT INTO Symptoms (name) VALUES (?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, symptom.name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
